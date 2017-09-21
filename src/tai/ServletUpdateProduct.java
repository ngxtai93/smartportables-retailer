package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Double;
import java.lang.StringBuilder;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class ServletUpdateProduct extends HttpServlet {

    private final String MIME_PNG = "image/png";
    private final String MIME_JPG = "image/jpeg";
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

            // process uploaded items
            updateProductToCatalog(req, updatedProduct);

            req.getSession().setAttribute("command-executed", "product-update");
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

    private Product buildProductObject(Map<String, String> productParam) {
        Product product = new Product();
        product.setCategory (productParam.get("category"));
        product.setId       (Integer.parseInt(productParam.get("product-id")));
        
        String discount = productParam.get("discount");
        String name = productParam.get("name");
        String price = productParam.get("price");
        String image = productParam.get("image");
        // can be null. If null -> no update
        if(discount != null && !discount.equals("")) {
            product.setDiscount (Double.parseDouble(discount));
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
        return product;
    }

    private void updateProductToCatalog(HttpServletRequest req, Product product) {
        String xmlFilePath = req.getServletContext().getRealPath("resources/data/ProductCatalog.xml");
        Document doc = getXmlDocument(xmlFilePath);
        Element elementToBeUpdated = findProductElement(doc, product);
        updateElement(elementToBeUpdated, product);

        writeToXml(doc, xmlFilePath);
        // write back to file
        
    }

    
    private Document getXmlDocument(String filePath) {
        Document doc = null;
        try {
            doc =   DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(filePath);
        }
        catch(ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    private Element findProductElement(Document doc, Product product) {
        XPath xpath =   XPathFactory.newInstance()
                        .newXPath();
        String exprStr =      "/ProductCatalog"
                            + "/category[@id=\'" + product.getCategory() + "\']"
                            + "/product[@id=\'" + product.getId() + "\']"
        ;
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
        nameElement.setTextContent(product.getName());
        Element priceElement = doc.createElement("price");
        priceElement.setTextContent(String.valueOf(product.getPrice()));
        Element discountElement = doc.createElement("discount");
        discountElement.setTextContent(String.valueOf(product.getDiscount()));

        // append to new element
        newProductElement.appendChild(imageElement);
        newProductElement.appendChild(nameElement);
        newProductElement.appendChild(priceElement);
        newProductElement.appendChild(discountElement);

        return newProductElement;
    }

    private void updateElement(Element origin, Product product) {
        // update element
        if(product.getImage() != null) {
            Element imageElement = (Element) origin.getElementsByTagName("image").item(0);
            imageElement.setTextContent(product.getImage());
        }
        if(product.getName() != null) {
            Element nameElement = (Element) origin.getElementsByTagName("name").item(0);
            nameElement.setTextContent(product.getName());
        }
        if(product.getPrice() != null) {
            Element priceElement = (Element) origin.getElementsByTagName("price").item(0);
            priceElement.setTextContent(String.valueOf(product.getPrice()));
        }
        if(product.getDiscount() != null) {
            Element discountElement = (Element) origin.getElementsByTagName("discount").item(0);
            discountElement.setTextContent(String.valueOf(product.getDiscount()));
        }
    }
    private void writeToXml(Document doc, String xmlFilePath) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer =   tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            OutputStream out = new FileOutputStream(new File(xmlFilePath));
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            Result output = new StreamResult(writer);
            Source input = new DOMSource(doc);

            transformer.transform(input, output);

            writer.close();
        }
        catch(TransformerException | IOException e) {
            e.printStackTrace();
        }
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