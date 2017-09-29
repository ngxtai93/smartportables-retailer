package tai;


import java.io.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class OrderManager {

    private final String ORDER_INFO_PATH = "resources/data/user/Order.xml";
    private XmlUtilities xmlUtil = XmlUtilities.INSTANCE;

    public Order processOrderPlaced(HttpServletRequest req, HttpServletResponse res) {
        Order order = buildOrder(req);

        // add to xml
        addToOrderFile(req, res, order);

        return order;
    }

    private Order buildOrder(HttpServletRequest req) {
        User currentUser = (User) req.getSession().getAttribute("currentUser");

        Order order = new Order();        
        LocalDate orderDate = LocalDate.now();
        // populate order
        order.setUsername(currentUser.getUsername());
        order.setListProduct(currentUser.getShoppingCart().getListProduct());
        order.setOrderDate(orderDate);
        order.setConfirmNumber(Long.valueOf(StringUtilities.INSTANCE.generateRandomNumber(7)));
        order.setDeliverDate(orderDate.plusWeeks(2));
    

        order.setName(req.getParameter("name"));
        order.setAddress(req.getParameter("address"));
        order.setCity(req.getParameter("city"));
        order.setState(req.getParameter("state"));
        order.setZip(Integer.valueOf(req.getParameter("zip")));
        order.setPhone(Integer.valueOf(req.getParameter("phone")));
    
        order.setCreditCardNum(new BigInteger((req.getParameter("cc-num"))));
        // date expiration
        String ccExp = req.getParameter("cc-exp");
        LocalDate expDate = convertExpirationToLocalDate(ccExp);
        order.setExpireDate(expDate);

        order.setStatus("placed");
        return order;
    }

    private void addToOrderFile(HttpServletRequest req, HttpServletResponse res, Order order) {
        String filePath = req.getServletContext().getRealPath(ORDER_INFO_PATH);
        Document document = xmlUtil.getXmlDocument(filePath);
        Element rootElement = (Element) document.getFirstChild();

        int productCount = rootElement.getElementsByTagName("order").getLength();
        order.setId(Integer.valueOf(productCount + 1));
        Element orderElement = buildOrderElement(document, order);

        rootElement.appendChild(orderElement);
        xmlUtil.writeToXml(document, filePath);
    }

    private Element buildOrderElement(Document doc, Order order) {
        // create order element: <order id="x" username="x">
        Element orderElement = doc.createElement("order");
        orderElement.setAttribute("id", String.valueOf(order.getId()));
        orderElement.setAttribute("username", order.getUsername());
        orderElement.setAttribute("order-date", order.getOrderDate().toString());
        orderElement.setAttribute("deliver-date", order.getDeliverDate().toString());
        orderElement.setAttribute("confirm-number", String.valueOf(order.getConfirmNumber()));


        // product elements
        List<Element> listProductElement = buildListProductElement(doc, order);

        // customer info element
        Element customerInfoElement = doc.createElement("customer-info");
        buildCustomerInfoElement(customerInfoElement, doc, order);

        // credit card element
        Element creditCardElement = doc.createElement("credit-card");
        buildCreditCardElement(creditCardElement, doc, order);

        // status element
        Element statusElement = doc.createElement("status");
        statusElement.setTextContent(order.getStatus());

        for(Element element: listProductElement) {
            orderElement.appendChild(element);
        }
        orderElement.appendChild(customerInfoElement);
        orderElement.appendChild(creditCardElement);
        orderElement.appendChild(statusElement);

        return orderElement;
    }

    private List<Element> buildListProductElement(Document doc, Order order) {
        List<Element> list = new ArrayList<>();
        LinkedHashMap<Product, Integer> mapProduct = order.getListProduct();
        for(Map.Entry<Product, Integer> entry: mapProduct.entrySet()) {
            Product product = entry.getKey();
            Integer amount = entry.getValue();

            Element productElement = doc.createElement("product");

            Element categoryElement = doc.createElement("category");
            categoryElement.setTextContent(product.getCategory());
            Element productIdElement = doc.createElement("product-id");
            productIdElement.setTextContent(String.valueOf(product.getId()));
            Element amountElement = doc.createElement("amount");
            amountElement.setTextContent(amount.toString());

            productElement.appendChild(categoryElement);
            productElement.appendChild(productIdElement);
            productElement.appendChild(amountElement);

            list.add(productElement);
        }
        
        return list;
    }

    private void buildCustomerInfoElement(Element customerInfoElement, Document doc, Order order) {
        Element nameElement = doc.createElement("name");
        nameElement.setTextContent(order.getName());
        Element addressElement = doc.createElement("address");
        addressElement.setTextContent(order.getAddress());
        Element cityElement = doc.createElement("city");
        cityElement.setTextContent(order.getCity());
        Element stateElement = doc.createElement("state");
        stateElement.setTextContent(order.getState());
        Element zipElement = doc.createElement("zip");
        zipElement.setTextContent(order.getZip().toString());
        Element phoneElement = doc.createElement("phone");
        phoneElement.setTextContent(order.getPhone().toString());

        customerInfoElement.appendChild(nameElement);
        customerInfoElement.appendChild(addressElement);
        customerInfoElement.appendChild(cityElement);
        customerInfoElement.appendChild(stateElement);
        customerInfoElement.appendChild(zipElement);
        customerInfoElement.appendChild(phoneElement);

    }

    private void buildCreditCardElement(Element creditCardElement, Document doc, Order order) {
        Element numberElement = doc.createElement("number");
        BigInteger creditCardNum = order.getCreditCardNum();

        String ccStr = processCreditCardNum(creditCardNum);
        
        numberElement.setTextContent(ccStr);

        // expiration to string
        LocalDate expDate = order.getExpireDate();
        int month = expDate.getMonthValue();
        int year = expDate.getYear();

        String monthStr = "";
        String yearStr = "";

        if(month / 10 == 0) {
            monthStr += "0";
        }
        monthStr += String.valueOf(month);

        // take only 2 latter numbers
        year = year % 100;
        if(year / 10 == 0) {
            yearStr += "0";
        }
        yearStr += String.valueOf(year);

        Element expireElement = doc.createElement("expire");
        expireElement.setTextContent(monthStr + yearStr);

        creditCardElement.appendChild(numberElement);
        creditCardElement.appendChild(expireElement);
    }
    private LocalDate convertExpirationToLocalDate(String creditCardExpire) {
        String[] expSplit = creditCardExpire.split("/");
        int month = Integer.parseInt(expSplit[0]);
        int year = Integer.parseInt(expSplit[1]) + 2000;

        LocalDate expDate = LocalDate.of(year, month, 1);
        expDate = expDate.plusMonths(1).minusDays(1);   // last day of that month

        return expDate;
    }

    private String processCreditCardNum(BigInteger ccNum) {
        // get number of digits in creditCardNum
        int digit = 0;
        BigInteger tmp = ccNum;
        while(!tmp.equals(BigInteger.ZERO)) {
            tmp = tmp.divide(BigInteger.TEN);
            digit++;
        }
        int digitToAdd = 16 - digit;
        String ccStr = "";
        for(int i = 0; i < digitToAdd; i++) {
            ccStr += "0";
        }
        ccStr += ccNum.toString();

        return ccStr;
    }
}