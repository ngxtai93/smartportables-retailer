package tai.model;


import java.io.*;
import java.math.BigInteger;
import java.time.DayOfWeek;
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

import tai.entity.Order;
import tai.entity.Product;
import tai.entity.User;
import tai.utils.*;
import tai.sax.SaxOrderHandler;

public class OrderManager {

    private final String ORDER_INFO_PATH = "resources/data/user/Order.xml";
    private XmlUtilities xmlUtil = XmlUtilities.INSTANCE;
    private MySQLDataStoreUtilities mysqlUtil = MySQLDataStoreUtilities.INSTANCE;

    public Order processOrderPlaced(HttpServletRequest req, HttpServletResponse res) {
        Order order = buildOrder(req);
        User user = (User) req.getSession().getAttribute("currentUser");

        // add to xml
        // addToOrderFile(req, res, order);


        // insert to mysql db
        insertOrder(req, order, user);
        return order;
    }
    
    public void insertOrder(HttpServletRequest req, Order order, User user) {
        mysqlUtil.insertOrder(req.getServletContext(), order, user);
    }

    public List<Order> getListOrder(HttpServletRequest req, User user) {
        List<Order> listOrder =  mysqlUtil.selectOrder(req.getServletContext(), user);
        if(listOrder != null) {
            populateListOrder(listOrder, req.getServletContext());
        }
        return listOrder;
        
    }

    public List<Order> getListAllOrder(HttpServletRequest req) {
        File orderFile = new File(req.getServletContext().getRealPath(ORDER_INFO_PATH));
        ArrayList<Order> listOrder = buildListOrder(orderFile);
        populateListOrder(listOrder, req.getServletContext());

        return listOrder;
    }

    public boolean canCancel(HttpServletRequest req, Integer id) {
        boolean result = false;

        List<Order> listAllOrder = getListAllOrder(req);
        Order order = null;

        for(Order o: listAllOrder) {
            if(o.getId().equals(id)) {
                order = o;
                break;
            }
        }

        // current date and delivery date has to be at least 5 business days apart
        LocalDate date = LocalDate.now();
        int businessDay = 0;
        while(date.isBefore(order.getDeliverDate())) {
            date = date.plusDays(1);
            DayOfWeek dow = date.getDayOfWeek();
            if(dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                businessDay++;
            }
        }

        if(businessDay >= 5) {
            result = true;
        }

        return result;
    }

    public void cancelOrder(HttpServletRequest req, Integer id) {
        String filePath = req.getServletContext().getRealPath(ORDER_INFO_PATH);
        Document document = xmlUtil.getXmlDocument(filePath);

        Element orderElement = findOrderById(document, id);

        Element statusElement = (Element) orderElement.getElementsByTagName("status").item(0);
        statusElement.setTextContent("Cancelled");

        xmlUtil.writeToXml(document, filePath);
    }

    public void deleteOrder(HttpServletRequest req, Order order) {
        String filePath = req.getServletContext().getRealPath(ORDER_INFO_PATH);
        Document document = xmlUtil.getXmlDocument(filePath);

        Element orderElement = findOrderById(document, order.getId());
        orderElement.getParentNode().removeChild(orderElement);

        xmlUtil.writeToXml(document, filePath);
    }

    public void updateOrder(HttpServletRequest req, Order order) {
        // String filePath = req.getServletContext().getRealPath(ORDER_INFO_PATH);
        // Document document = xmlUtil.getXmlDocument(filePath);

        // Element orderElement = findOrderById(document, order.getId());
        // updateOrderElement(orderElement, order);

        // xmlUtil.writeToXml(document, filePath);

        mysqlUtil.updateOrder(req.getServletContext(), order.getId().intValue(), order);
    }

    private void updateOrderElement(Element orderElement, Order order) {
        Element nameElement = (Element) orderElement.getElementsByTagName("name").item(0);
        nameElement.setTextContent(order.getName());
        Element addressElement = (Element) orderElement.getElementsByTagName("address").item(0);
        addressElement.setTextContent(order.getAddress());
        Element cityElement = (Element) orderElement.getElementsByTagName("city").item(0);
        cityElement.setTextContent(order.getCity());
        Element stateElement = (Element) orderElement.getElementsByTagName("state").item(0);
        stateElement.setTextContent(order.getState());
        Element zipElement = (Element) orderElement.getElementsByTagName("zip").item(0);
        zipElement.setTextContent(order.getZip().toString());
        Element phoneElement = (Element) orderElement.getElementsByTagName("phone").item(0);
        phoneElement.setTextContent(order.getPhone().toString());

        Element creditCardElement = (Element) orderElement.getElementsByTagName("number").item(0);
        creditCardElement.setTextContent(order.getCreditCardNum().toString());
        Element expireElement = (Element) orderElement.getElementsByTagName("expire").item(0);
        expireElement.setTextContent(order.getShortExpDate());

        orderElement.setAttribute("deliver-date", order.getDeliverDate().toString());
    }


    private Element findOrderById(Document doc, Integer id) {
        XPath xpath =   XPathFactory.newInstance()
        .newXPath();
        String exprStr =    "/Order"
                            + "/order[@id=\'" + id + "\']"
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

    private ArrayList<Order> buildListOrder(File xmlFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            InputStream xmlInput = new FileInputStream(xmlFile);
            SAXParser saxParser = factory.newSAXParser();
            SaxOrderHandler orderHandler = new SaxOrderHandler();
            saxParser.parse(xmlInput, orderHandler);
            return orderHandler.listOrder;
        }
        catch(IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void populateListOrder(List<Order> listOrder, ServletContext sc) {
        ProductManager pm = new ProductManager();
        List<Product> listAllProduct = pm.getListProduct(sc);
        for(Order order: listOrder) {
            LinkedHashMap<Product, Integer> listItem = order.getListProduct();
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

    private Order buildOrder(HttpServletRequest req) {
        User currentUser = (User) req.getSession().getAttribute("currentUser");
        LinkedHashMap<Product, Integer> listProduct = currentUser.getShoppingCart().getListProduct();
        return buildOrder(req, currentUser, listProduct);

    }

    public Order buildOrder(HttpServletRequest req, User user, LinkedHashMap<Product, Integer> listProduct) {
        Order order = new Order();        
        LocalDate orderDate = LocalDate.now();
        // populate order
        order.setUsername(user.getUsername());
        order.setListProduct(listProduct);
        order.setOrderDate(orderDate);
        order.setConfirmNumber(Long.valueOf(StringUtilities.INSTANCE.generateRandomNumber(7)));
        order.setDeliverDate(orderDate.plusWeeks(2));
    

        order.setName(req.getParameter("name"));
        order.setAddress(req.getParameter("address"));
        order.setCity(req.getParameter("city"));
        order.setState(req.getParameter("state"));
        order.setZip(Integer.valueOf(req.getParameter("zip")));
        order.setPhone(Long.valueOf(req.getParameter("phone")));

        order.setCreditCardNum(Long.valueOf((req.getParameter("cc-num"))));
        // date expiration
        String ccExp = req.getParameter("cc-exp");
        LocalDate expDate = convertExpirationToLocalDate(ccExp);
        order.setExpireDate(expDate);

        order.setStatus("Placed");
        return order;        
    }

    public void addToOrderFile(HttpServletRequest req, HttpServletResponse res, Order order) {
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
        Long creditCardNum = order.getCreditCardNum();

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
    public LocalDate convertExpirationToLocalDate(String creditCardExpire) {
        String[] expSplit = creditCardExpire.split("/");
        int month = Integer.parseInt(expSplit[0]);
        int year = Integer.parseInt(expSplit[1]) + 2000;

        LocalDate expDate = LocalDate.of(year, month, 1);
        expDate = expDate.plusMonths(1).minusDays(1);   // last day of that month

        return expDate;
    }

    private String processCreditCardNum(Long ccNum) {
        // get number of digits in creditCardNum
        int digit = 0;
        long tmp = ccNum.longValue();
        while(tmp != 0) {
            tmp = tmp / 10;
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