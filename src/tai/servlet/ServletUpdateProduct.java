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

public class ServletUpdateProduct extends HttpServlet {

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

            req.getSession().setAttribute("command-executed", "product-update");
            res.sendRedirect(req.getContextPath() + "/success");
        }
    }

	private void processPostRequest(HttpServletRequest req) {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		File repository = (File) req.getServletContext().getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);
		factory.setSizeThreshold(1024 * 1024 * 5);  // 5MB

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(1024 * 1024 * 2);         // 2MB

		Product updatedProduct = null;
		try {
		    List<FileItem> listItem = upload.parseRequest(req);
		    Map<String, String> productParam = new HashMap<>();
		    for(FileItem fi: listItem) {
		        if(fi.isFormField()) {  // param from form input
		            productParam.put(fi.getFieldName(), fi.getString());
		        }
		        else {
		            uploadFile(req, productParam, fi);
		        }
		    }
		    updatedProduct = buildProductObject(productParam);
		}
		catch(FileUploadException e) {
		    e.printStackTrace();
		}

		if(
				   updatedProduct.getDiscount() == null
				&& updatedProduct.getRebate() == null
				&& updatedProduct.getName() == null
				&& updatedProduct.getPrice() == null
				&& updatedProduct.getImage() == null
				&& updatedProduct.getAmount() == null
		) {
			return;
		}
		// process uploaded items
		pm.updateProduct(updatedProduct);
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

    private Product buildProductObject(Map<String, String> productParam) {
        Product product = new Product();
        product.setCategory (productParam.get("category"));
        product.setId       (Integer.parseInt(productParam.get("product-id")));
        
        String discount = productParam.get("discount");
        String rebate = productParam.get("rebate");
        String name = productParam.get("name");
        String price = productParam.get("price");
        String image = productParam.get("image");
        String amount = productParam.get("amount");
        // can be null. If null -> no update
        if(discount != null && !discount.equals("")) {
            product.setDiscount (Double.parseDouble(discount));
        }
        if(rebate != null && !rebate.equals("")) {
            product.setRebate (Double.parseDouble(rebate));
        }
        if(name != null && !name.equals("")) {
            product.setName     (name);
        }
        if(price != null && !price.equals("")) {
            product.setPrice    (Double.parseDouble(price));
        }
        if(image != null && !image.equals("")) {
            product.setImage    (image);
        }
        if(amount != null && !amount.equals("")) {
            product.setAmount    (Integer.valueOf(amount));
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