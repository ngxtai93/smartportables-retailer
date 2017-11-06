package tai.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


import tai.entity.Product;
import tai.entity.Role;
import tai.entity.User;
import tai.model.ProductManager;
import tai.utils.StringUtilities;

public class ServletAddProduct extends HttpServlet {

    private final String MIME_PNG = "image/png";
    private final String MIME_JPG = "image/jpeg";
    private StringUtilities stringUtil = StringUtilities.INSTANCE;
    private ProductManager pm = new ProductManager();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
        if(user.getRole() != Role.STORE_MANAGER || !ServletFileUpload.isMultipartContent(req)) {
            res.sendRedirect(req.getContextPath());
        }
        else {
            processPostRequest(req);

            req.getSession().setAttribute("command-executed", "product-add");
            res.sendRedirect(req.getContextPath() + "/success");
        }
    }

    /**
     * Process request to add product
     */
	private void processPostRequest(HttpServletRequest req) {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		File repository = (File) req.getServletContext().getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);
		factory.setSizeThreshold(1024 * 1024 * 5);  // 5MB

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(1024 * 1024 * 2);         // 2MB

		Product product = null;
		try {
		    List<FileItem> listItem = upload.parseRequest(req);
		    Map<String, String> productParam = new HashMap<>();
		    List<Integer> listAccessoryId = new ArrayList<>();
		    for(FileItem fi: listItem) {
		        if(fi.isFormField()) {  // param from form input
		            if(fi.getFieldName().equals("accessory-id")) {
		                listAccessoryId.add(Integer.valueOf(fi.getString()));
		            }
		            else {
		                productParam.put(fi.getFieldName(), fi.getString());
		            }
		        }
		        else {
		            uploadFile(req, productParam, fi);
		        }
		    }
		    
		    product = buildProductObject(productParam, listAccessoryId);
		}
		catch(FileUploadException e) {
		    e.printStackTrace();
		}

		// process uploaded items
		pm.addProduct(product);
	}

    private void uploadFile(HttpServletRequest req, Map<String, String> productParam, FileItem fi) {
        String extension = null;
        switch(fi.getContentType()) {
            case MIME_JPG:
                extension = ".jpg";
                break;
            case MIME_PNG:
                extension = ".png";
                break;
        }
        if(extension != null) {
            File file = getFilePath(req, productParam, extension);
            productParam.put("image", file.getName());
            // do upload file
            try {
                fi.write(file);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Product buildProductObject(Map<String, String> productParam, List<Integer> listAccessoryId) {
        Product product = new Product();
        product.setCategory (productParam.get("category"));
        if(productParam.get("discount").equals("")) {
            product.setDiscount(Double.valueOf(0));
        }
        else {
            product.setDiscount (Double.parseDouble(productParam.get("discount")));
        }
        if(productParam.get("rebate").equals("")) {
            product.setRebate(Double.valueOf(0));
        }
        else {
            product.setRebate (Double.parseDouble(productParam.get("rebate")));
        }
        if(productParam.get("amount").equals("")) {
            product.setAmount(Integer.valueOf(0));
        }
        else {
            product.setAmount (Integer.parseInt(productParam.get("amount")));
        }
        product.setName     (productParam.get("name"));
        product.setPrice    (Double.parseDouble(productParam.get("price")));
        product.setImage    (productParam.get("image"));
        if(listAccessoryId.size() > 0) {
            product.setListAccessoryId(listAccessoryId);
        }

        return product;
    }

    private File getFilePath(HttpServletRequest req, Map<String, String> productParam, String extension) {
        String category = productParam.get("category");
        String uploadFilePath = req.getServletContext().getRealPath("resources/images/product/")
                                + "\\"
                                + category
                                + "\\"
        ;
        String fileName = stringUtil.generateRandomString(10) + extension;
        File file = new File(uploadFilePath + fileName);
        while(file.exists()) {
            fileName = stringUtil.generateRandomString(10) + extension;
            file = new File(uploadFilePath + fileName);
        }
        return file;
    }
}