package tai.sax;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.ArrayList;

import tai.utils.StringUtilities;
import tai.entity.ShoppingCart;
import tai.entity.Product;

public class SaxCartHandler extends DefaultHandler {
    public ArrayList<ShoppingCart> listShoppingCart;
    private ShoppingCart cart;
    private String elementValueRead;
    private Product product;
    private StringUtilities stringUtil = StringUtilities.INSTANCE;
    private int amount;

    public SaxCartHandler() {
        listShoppingCart = new ArrayList<>();
    }

    @Override
    public void startElement(String str1, String str2, String elementName, Attributes attributes) throws SAXException {
        if(elementName.equalsIgnoreCase("cart")) {
            cart = new ShoppingCart();
            cart.setCartId(attributes.getValue("id"));
        }    
        if(elementName.equalsIgnoreCase("product")) {
            product = new Product();
        }
    }

    @Override
    public void endElement(String str1, String str2, String element) throws SAXException {    
        if (element.equalsIgnoreCase("category")) {
            product.setCategory(stringUtil.filter(elementValueRead));
            return;
        }
        if (element.equalsIgnoreCase("product-id")) {
            product.setId(Integer.valueOf(stringUtil.filter(elementValueRead)));
            return;
        }
        if (element.equalsIgnoreCase("amount")) {
            amount = Integer.parseInt(stringUtil.filter(elementValueRead));
            return;
        }
        if (element.equalsIgnoreCase("product")) {
            cart.getListProduct().put(product, Integer.valueOf(amount));
            product = null;
            amount = 0;
            return;
        }
        if(element.equalsIgnoreCase("cart")) {
            listShoppingCart.add(cart);
            return;
        }

    }

    @Override
    public void characters(char[] content, int begin, int end) throws SAXException {
        elementValueRead = new String(content, begin, end);
    }


}