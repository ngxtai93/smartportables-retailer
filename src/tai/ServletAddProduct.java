package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;

import java.io.*;
import java.util.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class ServletAddProduct extends HttpServlet {

    private final String MIME_PNG = "image/png";
    private final String MIME_JPG = "image/jpeg";
    private StringUtilities stringUtil = StringUtilities.INSTANCE;
    private XmlUtilities xmlUtil = XmlUtilities.INSTANCE;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
        if(user.getRole() != Role.STORE_MANAGER || !ServletFileUpload.isMultipartContent(req)) {
            res.sendRedirect(req.getContextPath());
        }
        else {
            // process request
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
                ArrayList<Integer> listAccessoryId = new ArrayList<>();
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
            addProductToCatalog(req, product);

            req.getSession().setAttribute("command-executed", "product-add");
            res.sendRedirect(req.getContextPath() + "/success");
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

    private Product buildProductObject(Map<String, String> productParam, ArrayList<Integer> listAccessoryId) {
        Product product = new Product();
        product.setCategory (productParam.get("category"));
        if(productParam.get("discount").equals("")) {
            product.setDiscount(Double.valueOf(0));
        }
        else {
            product.setDiscount (Double.parseDouble(productParam.get("discount")));
        }
        product.setName     (productParam.get("name"));
        product.setPrice    (Double.parseDouble(productParam.get("price")));
        product.setImage    (productParam.get("image"));
        if(listAccessoryId.size() > 0) {
            product.setListAccessoryId(listAccessoryId);
        }

        return product;
    }

    private void addProductToCatalog(HttpServletRequest req, Product product) {
        String xmlFilePath = req.getServletContext().getRealPath("resources/data/ProductCatalog.xml");
        Document doc = xmlUtil.getXmlDocument(xmlFilePath);
        Element categoryElement = findCategoryElement(doc, product);
        
        int productCount = categoryElement.getElementsByTagName("product").getLength();

        // create element
        Element newProductElement = createNewProductElement(doc, product, productCount);
        categoryElement.appendChild(newProductElement);

        xmlUtil.writeToXml(doc, xmlFilePath);
        // write back to file
        
    }

    private Element findCategoryElement(Document doc, Product product) {
        XPath xpath =   XPathFactory.newInstance()
                        .newXPath();
        String exprStr = "/ProductCatalog/category[@id=\'" + product.getCategory() + "\']";
        NodeList nl = null;
        try {
            XPathExpression expr = xpath.compile(exprStr);
            nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        }
        catch(XPathExpressionException e) {
            e.printStackTrace();
        }
        
        return (Element) nl.item(0);
    }

    private Element createNewProductElement(Document doc, Product product, int productCount) {
        int nextProductId = productCount + 1;

        Element newProductElement = doc.createElement("product");
        newProductElement.setAttribute("id", String.valueOf(nextProductId));

        // create subelement of product
        Element imageElement = doc.createElement("image");
        imageElement.setTextContent(product.getImage());
        Element nameElement = doc.createElement("name");
        // we use CDATA for formatting
        CDATASection nameCData = doc.createCDATASection(product.getName());
        nameElement.appendChild(nameCData);
        Element priceElement = doc.createElement("price");
        priceElement.setTextContent(String.valueOf(product.getPrice()));
        Element discountElement = doc.createElement("discount");
        discountElement.setTextContent(String.valueOf(product.getDiscount()));
        
        ArrayList<Integer> listAccessory = product.getListAccessoryId();
        Element accessoriesElement = null;
        if(listAccessory != null) {
            accessoriesElement = doc.createElement("accessories");
            for(Integer accessoryId: listAccessory) {
                Element idElement = doc.createElement("product-id");
                idElement.setAttribute("id", String.valueOf(accessoryId));
                accessoriesElement.appendChild(idElement);
            }
        }

        // append to new element
        newProductElement.appendChild(imageElement);
        newProductElement.appendChild(nameElement);
        newProductElement.appendChild(priceElement);
        newProductElement.appendChild(discountElement);
        if(listAccessory != null) {
            newProductElement.appendChild(accessoriesElement);
        }

        return newProductElement;
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