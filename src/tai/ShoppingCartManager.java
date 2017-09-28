package tai;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;


public class ShoppingCartManager {

    private final String CART_INFO_PATH = "resources/data/user/CartInfo.xml";
    private XmlUtilities xmlUtil = XmlUtilities.INSTANCE;

    public ShoppingCart getCart(ServletContext sc, User user) {
        ArrayList<ShoppingCart> listAllCart = getListCart(sc);
        String cartId = buildCartId(user);

        ShoppingCart result = null;
        for(ShoppingCart cart: listAllCart) {
            if(cart.getCartId().equals(cartId)) {
                result = cart;
                break;
            }
        }
        return result;
    }

    public void addToCart(HttpServletRequest req, HttpServletResponse res, User user, String category, Integer id) {
        String cartId = buildCartId(user);
        String filePath = req.getServletContext().getRealPath(CART_INFO_PATH);
        Document document = xmlUtil.getXmlDocument(filePath);
        Element cartElement = findCartElement(document, cartId);
        if(cartElement == null) {
            cartElement = createNewCartElement(document, cartId);
            Element cartInfoElement = (Element) document.getFirstChild();
            cartInfoElement.appendChild(cartElement);
        }

        Element productElement = findExistingProduct(document, cartId, category, id);
        if(productElement == null) {
            productElement = createNewProductElement(document, category, id);
            cartElement.appendChild(productElement);
        }
        else {
            Element amountElement = (Element) productElement.getElementsByTagName("amount").item(0);
            int amountIncrement = Integer.parseInt(amountElement.getTextContent());
            amountIncrement += 1;
            amountElement.setTextContent(String.valueOf(amountIncrement));
        }
        
        xmlUtil.writeToXml(document, filePath);
    }

    public void updateToCart(
        HttpServletRequest req, HttpServletResponse res, User user
        , String cartId, String category, Integer id, Integer amount) {
        String filePath = req.getServletContext().getRealPath(CART_INFO_PATH);
        Document document = xmlUtil.getXmlDocument(filePath);
        Element cartElement = findCartElement(document, cartId);
        Element productElement = findExistingProduct(document, cartId, category, id);

        Element amountElement = (Element) productElement.getElementsByTagName("amount").item(0);
        amountElement.setTextContent(String.valueOf(amount));
        
        xmlUtil.writeToXml(document, filePath);
    }

    private Element createNewCartElement(Document doc, String cartId) {
        Element newCartElement = doc.createElement("cart");
        newCartElement.setAttribute("id", cartId);

        return newCartElement;
    }

    private Element findExistingProduct(Document doc, String cartId, String category, Integer id) {
        XPath xpath =   XPathFactory.newInstance()
        .newXPath();
        String exprStr =    "/CartInfo"
                            + "/cart[@id=\'" + cartId + "\']"
                            + "/product"
                            + "[category=\'" + category + "\'"
                            + " and product-id=\'" + id + "\']"
        ;
        NodeList nl = null;
        try {
            XPathExpression expr = xpath.compile(exprStr);
            nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        }
        catch(XPathExpressionException e) {
            e.printStackTrace();
        }

        return (nl == null ? null : (Element) nl.item(0));
    }

    private Element createNewProductElement(
        Document doc, String category, Integer productId) {
        
        // create product
        Element productElement = doc.createElement("product");

        // create subelement of product
        Element categoryElement = doc.createElement("category");
        categoryElement.setTextContent(category);
        Element productIdElement = doc.createElement("product-id");
        productIdElement.setTextContent(String.valueOf(productId));
        Element amountElement = doc.createElement("amount");
        amountElement.setTextContent(String.valueOf("1"));

        // append to new element
        productElement.appendChild(categoryElement);
        productElement.appendChild(productIdElement);
        productElement.appendChild(amountElement);

        return productElement;
    }

    private Element findCartElement(Document doc, String cartId) {
        XPath xpath =   XPathFactory.newInstance()
        .newXPath();
        String exprStr =    "/CartInfo"
                            + "/cart[@id=\'" + cartId + "\']"
        ;
        NodeList nl = null;
        try {
            XPathExpression expr = xpath.compile(exprStr);
            nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        }
        catch(XPathExpressionException e) {
            e.printStackTrace();
        }

        return (nl == null ? null : (Element) nl.item(0));
    }

    private ArrayList<ShoppingCart> getListCart(ServletContext sc) {
        File productCatalogFile = new File(sc.getRealPath(CART_INFO_PATH));
        ArrayList<ShoppingCart> listCart = buildListCart(productCatalogFile);
        // populate product in cart, given category and id
        populateListCart(listCart, sc);

        return listCart;
    }

    private ArrayList<ShoppingCart> buildListCart(File xmlFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            InputStream xmlInput = new FileInputStream(xmlFile);
            SAXParser saxParser = factory.newSAXParser();
            SaxCartHandler cartHandler = new SaxCartHandler();
            saxParser.parse(xmlInput, cartHandler);
            return cartHandler.listShoppingCart;
        }
        catch(IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void populateListCart(ArrayList<ShoppingCart> listCart, ServletContext sc) {
        ProductManager pm = new ProductManager();
        List<Product> listAllProduct = pm.getListProduct(sc);
        for(ShoppingCart cart: listCart) {
            LinkedHashMap<Product, Integer> listItem = cart.getListProduct();
            for(Map.Entry<Product, Integer> entry: listItem.entrySet()) {
                Product p = entry.getKey();
                for(Product product: listAllProduct) {
                    if(p.getCategory().equals(product.getCategory()) && p.getId().equals(product.getId())) {
                        p.setName(product.getName());
                        p.setImage(product.getImage());
                        p.setPrice(product.getPrice());
                        p.setDiscount(product.getDiscount());
                        p.setListAccessoryId(product.getListAccessoryId());
                        break;
                    }
                }
            }
        }
    }

    private String buildCartId(User user) {
        String cartId = null;
        //[role]-[username]
        switch(user.getRole()) {
            case CUSTOMER:
                cartId = "customer";
                break;
            case SALESMAN:
                cartId = "salesman";
                break;
            case STORE_MANAGER:
                cartId = "storemanager";
                break;
        }
        cartId += "-" + user.getUsername();

        return cartId;
    }
}