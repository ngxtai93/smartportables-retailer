package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            // System.out.println(factory.getSizeThreshold());

            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(1024 * 1024 * 2);         // 2MB
            try {
                List<FileItem> listItem = upload.parseRequest(req);
                Map<String, String> productParam = new HashMap<>();
                for(FileItem fi: listItem) {
                    if(fi.isFormField()) {
                        productParam.put(fi.getFieldName(), fi.getString());
                    }
                    else {
                        // System.out.println("fieldName: " + fi.getFieldName());
                        // System.out.println("fileName: " + fi.getName());
                        // System.out.println("contentType: " + fi.getContentType());
                        uploadFile(req, productParam, fi);
                    }
                }
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

        // build filePath
        String category = productParam.get("category");
        String uploadFilePath = req.getServletContext().getRealPath("resources/images/product/")
                                + "\\"
                                + category
                                + "\\"
        ;
        // System.out.println(uploadFilePath.toString());
        String fileName = StringUtilities.generateRandomString(10) + extension;
        File file = new File(uploadFilePath + fileName);
        while(file.exists()) {
            fileName = StringUtilities.generateRandomString(10) + extension;
            file = new File(uploadFilePath + fileName);
        }
        System.out.println("file name: " + fileName);
        // do upload file
        try {
            fi.write(file);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }
}