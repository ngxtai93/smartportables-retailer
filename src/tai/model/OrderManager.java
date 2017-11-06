package tai.model;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import tai.entity.Order;
import tai.entity.Product;
import tai.entity.User;
import tai.utils.*;

public class OrderManager {

    private MySQLDataStoreUtilities mysqlUtil = MySQLDataStoreUtilities.INSTANCE;
    public static final String[] LIST_STATUS = {"Placed", "Delivered", "Cancelled"};

    public Order processOrderPlaced(HttpServletRequest req, HttpServletResponse res) {
        Order order = buildOrder(req);

        // insert to mysql db
        insertOrder(req, order);

        // update amount of product
        Map<Product, Integer> mapProduct = order.getListProduct();
        ProductManager pm = new ProductManager();
        for(Map.Entry<Product, Integer> entry: mapProduct.entrySet()) {
            Product p = entry.getKey();
            Integer amount = entry.getValue();
            p.setAmount(p.getAmount() - amount);
            pm.updateProduct(req, p);
        }
        return order;
    }
    
    public void insertOrder(HttpServletRequest req, Order order) {
        mysqlUtil.insertOrder(req.getServletContext(), order);
    }

    public List<Order> getListOrder(HttpServletRequest req, User user) {
        List<Order> listOrder =  mysqlUtil.selectOrder(req.getServletContext(), user);
        if(listOrder != null) {
            populateListOrder(listOrder, req.getServletContext());
        }
        return listOrder;
        
    }

    public boolean canCancel(HttpServletRequest req, Integer id, User requestedUser) {
        boolean result = false;

        Order order = mysqlUtil.selectOrder(req.getServletContext(), id.intValue());
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
        ServletContext sc = req.getServletContext();
        Order order = mysqlUtil.selectOrder(sc, id.intValue());
        order.setStatus(LIST_STATUS[2]);
        mysqlUtil.updateOrder(sc, id.intValue(), order);
    }

    public void deleteOrder(HttpServletRequest req, Order order) {
        mysqlUtil.deleteOrder(req.getServletContext(), order);
    }

    public void updateOrder(HttpServletRequest req, Order order) {
        mysqlUtil.updateOrder(req.getServletContext(), order.getId().intValue(), order);
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
        order.setUser(user);
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

    public LocalDate convertExpirationToLocalDate(String creditCardExpire) {
        String[] expSplit = creditCardExpire.split("/");
        int month = Integer.parseInt(expSplit[0]);
        int year = Integer.parseInt(expSplit[1]) + 2000;

        LocalDate expDate = LocalDate.of(year, month, 1);
        expDate = expDate.plusMonths(1).minusDays(1);   // last day of that month

        return expDate;
    }
}