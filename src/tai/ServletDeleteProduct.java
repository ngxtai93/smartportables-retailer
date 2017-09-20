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

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class ServletDeleteProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
        if(user.getRole() != Role.STORE_MANAGER) {
            res.sendRedirect(req.getContextPath());
        }
        else {
            // process request
            String[] listIdToDelete = req.getParameterValues("product-id");
            if(listIdToDelete == null || listIdToDelete.length == 0) {
                Map<Integer, Product> mapProduct = new ProductManager().getListProduct(req, req.getParameter("category"));
                req.setAttribute("no-product-chosen", "true");
                req.setAttribute("mapProduct", mapProduct);
                req.getRequestDispatcher("/WEB-INF/jsp/product/product_delete.jsp").forward(req, res);
            }
            else {
                // process uploaded items
                deleteProduct(req, listIdToDelete);
            
                req.getSession().setAttribute("command-executed", "product-delete");
                res.sendRedirect(req.getContextPath() + "/success");
            }
            
        }
    }

    private void deleteProduct(HttpServletRequest req, String[] listProductId) {
        String xmlFilePath = req.getServletContext().getRealPath("resources/data/ProductCatalog.xml");
        Document doc = getXmlDocument(xmlFilePath);
        String category = req.getParameter("category");
        for(String id: listProductId) {
            Element elementToBeDeleted = findProductElement(doc, category, id);
            elementToBeDeleted.getParentNode().removeChild(elementToBeDeleted);
            // updateElement(elementToBeUpdated, product);
        }
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

    private Element findProductElement(Document doc, String category, String id) {
        XPath xpath =   XPathFactory.newInstance()
                        .newXPath();
        String exprStr =      "/ProductCatalog"
                            + "/category[@id=\'" + category + "\']"
                            + "/product[@id=\'" + id + "\']"
        ;
        System.out.println(exprStr);
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