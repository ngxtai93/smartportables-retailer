package tai;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;


public class ShoppingCartManager {

    private final String cartInfoPath = "resources/data/user/CartInfo.xml";


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

    private ArrayList<ShoppingCart> getListCart(ServletContext sc) {
        File productCatalogFile = new File(sc.getRealPath(cartInfoPath));
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
            for(Product p: cart.getListItem()) {
                for(Product product: listAllProduct) {
                    if(p.getCategory().equals(product.getCategory()) && p.getId().equals(product.getId())) {
                        p.setName(product.getName());
                        p.setImage(product.getImage());
                        p.setPrice(product.getPrice());
                        p.setDiscount(product.getDiscount());
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