package tai;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SaxOrderHandler extends DefaultHandler {
    public ArrayList<Order> listOrder;
    private Order order;
    private String elementValueRead;
    private StringUtilities stringUtil = StringUtilities.INSTANCE;
    public LinkedHashMap<Product, Integer> listProduct;
    private Product product;
    private Integer amount;

    public SaxOrderHandler() {
        listOrder = new ArrayList<>();
        listProduct = new LinkedHashMap<>();
    }

    @Override
    public void startElement(String str1, String str2, String elementName, Attributes attributes) throws SAXException {
        if(elementName.equals("order")) {
            order = new Order();
            order.setId             (Integer.valueOf(attributes.getValue("id")));
            order.setUsername       (attributes.getValue("username"));
            order.setConfirmNumber  (Long.valueOf(attributes.getValue("confirm-number")));
            order.setOrderDate      (LocalDate.parse(attributes.getValue("order-date")));
            order.setDeliverDate    (LocalDate.parse(attributes.getValue("deliver-date")));
            return;
        }
        if(elementName.equalsIgnoreCase("product")) {
            product = new Product();
            return;
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
            listProduct.put(product, Integer.valueOf(amount));
            product = null;
            amount = 0;
            return;
        }

        if (element.equalsIgnoreCase("name")) {
            order.setName(stringUtil.filter(elementValueRead));
            return;
        }
        if (element.equalsIgnoreCase("address")) {
            order.setAddress(stringUtil.filter(elementValueRead));
            return;
        }
        if (element.equalsIgnoreCase("city")) {
            order.setCity(stringUtil.filter(elementValueRead));
            return;
        }
        if (element.equalsIgnoreCase("state")) {
            order.setState(stringUtil.filter(elementValueRead));
            return;
        }
        if (element.equalsIgnoreCase("zip")) {
            order.setZip(Integer.valueOf(stringUtil.filter(elementValueRead)));
            return;
        }
        if (element.equalsIgnoreCase("phone")) {
            order.setPhone(Integer.valueOf(stringUtil.filter(elementValueRead)));
            return;
        }

        if (element.equalsIgnoreCase("number")) {
            order.setCreditCardNum(new BigInteger(stringUtil.filter(elementValueRead)));
            return;
        }
        if (element.equalsIgnoreCase("expire")) {
            String expire = stringUtil.filter(elementValueRead);
            LocalDate expDate = processExpDate(expire);
            order.setExpireDate(expDate);
            return;
        }

        if (element.equalsIgnoreCase("status")) {
            order.setStatus(stringUtil.filter(elementValueRead));
            return;
        }


        if (element.equals("order")) {
            order.setListProduct(listProduct);
            listOrder.add(order);
            return;
        }

    }

    @Override
    public void characters(char[] content, int begin, int end) throws SAXException {
        elementValueRead = new String(content, begin, end);
    }

    private LocalDate processExpDate(String xmlString) {
        // format xmlString: e.g. 0719
        String monthStr = xmlString.substring(0, 2);
        String yearStr = xmlString.substring(2, 4);

        int month = Integer.parseInt(monthStr);
        int year = 2000 + Integer.parseInt(yearStr);

        LocalDate date = LocalDate.of(year, month, 1);
        return date.plusMonths(1).minusDays(1);
    }

}