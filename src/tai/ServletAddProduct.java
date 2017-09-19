package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Double;
import java.lang.StringBuilder;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import tai.StringUtilities;
import tai.User;
import tai.Role;

public class ServletAddProduct extends HttpServlet {

    private final String MIME_PNG = "image/png";
    private final String MIME_JPG = "image/jpg";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
        if(user.getRole() != Role.STORE_MANAGER || !ServletFileUpload.isMultipartContent(req)) {
            res.sendRedirect(req.getContextPath());
        }
        else {
            System.out.println("is multipart");
            DiskFileItemFactory factory = new DiskFileItemFactory();
            File repository = (File) req.getServletContext().getAttribute("javax.servlet.context.tempdir");
            factory.setRepository(repository);
            factory.setSizeThreshold(1024 * 1024 * 5);  // 5MB

            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(1024 * 1024 * 2);         // 2MB
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
                Product product = buildProductObject(productParam);
                System.out.println("Category: " + product.getCategory());
                System.out.println("Name: " + product.getName());
                System.out.println("Image: " + product.getImage());
                System.out.println("Price: " + product.getPrice());
                System.out.println("Discount: " + product.getDiscount());
            }
            catch(FileUploadException e) {
                e.printStackTrace();
            }

            // process uploaded items

            res.sendRedirect(req.getContextPath());
        }
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

    private Product buildProductObject(Map<String, String> productParam) {
        Product product = new Product();
        product.setCategory (productParam.get("category"));
        product.setDiscount (Double.parseDouble(productParam.get("discount")));
        product.setName     (productParam.get("name"));
        product.setPrice    (Double.parseDouble(productParam.get("price")));
        product.setImage    (productParam.get("image"));

        return product;
    }

    private File getFilePath(HttpServletRequest req, Map<String, String> productParam, String extension) {
        String category = productParam.get("category");
        String uploadFilePath = req.getServletContext().getRealPath("resources/images/product/")
                                + "\\"
                                + category
                                + "\\"
        ;
        String fileName = StringUtilities.generateRandomString(10) + extension;
        File file = new File(uploadFilePath + fileName);
        while(file.exists()) {
            fileName = StringUtilities.generateRandomString(10) + extension;
            file = new File(uploadFilePath + fileName);
        }
        return file;
    }
}