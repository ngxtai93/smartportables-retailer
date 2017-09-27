package tai;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.ArrayList;

public class SaxProductHandler extends DefaultHandler {
    public ArrayList<Product> listProduct;
    private Product product;
    private String elementValueRead;
    private String category;

    public SaxProductHandler() {
        listProduct = new ArrayList<>();
    }

    @Override
    public void startElement(String str1, String str2, String elementName, Attributes attributes) throws SAXException {
        if(elementName.equalsIgnoreCase("category")) {
            category = attributes.getValue("id");
        }    
        if(elementName.equalsIgnoreCase("product")) {
            product = new Product();
            product.setId(Integer.parseInt(attributes.getValue("id")));
        }

        if(elementName.equalsIgnoreCase("accessories")) {
            product.setListAccessoryId(new ArrayList<Integer>());
        }

        if(elementName.equalsIgnoreCase("product-id")) {
            Integer id = Integer.valueOf(attributes.getValue("id"));
            product.getListAccessoryId().add(id);
        }
    }

    @Override
    public void endElement(String str1, String str2, String element) throws SAXException {    
        if (element.equalsIgnoreCase("product")) {
            product.setCategory(category);
            listProduct.add(product);
            return;
        }
        if (element.equalsIgnoreCase("image")) {
            product.setImage(elementValueRead);
            return;
        }
        if (element.equalsIgnoreCase("name")) {
            product.setName(elementValueRead);
            return;
        }
        if (element.equalsIgnoreCase("price")) {
            product.setPrice(Double.valueOf(elementValueRead));
            return;
        }
        if (element.equalsIgnoreCase("discount")) {
            product.setDiscount(Double.valueOf(elementValueRead));
            return;
        }
        if(element.equalsIgnoreCase("category")) {
            category = null;
            return;
        }

    }

    @Override
    public void characters(char[] content, int begin, int end) throws SAXException {
        elementValueRead = new String(content, begin, end);
    }


}