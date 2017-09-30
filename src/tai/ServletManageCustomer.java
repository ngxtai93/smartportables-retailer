package tai;

import java.math.BigInteger;
import java.time.LocalDate;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;

public class ServletManageCustomer extends HttpServlet {

    private Authenticator auth;
    private ProductManager pm;
    private OrderManager om;

    public ServletManageCustomer() {
        auth = new Authenticator();
        pm = new ProductManager();
        om = new OrderManager();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
        if(user == null) {
            res.sendRedirect(req.getContextPath() + "/login");
        }
        else {
            RequestDispatcher rd = null;
            String uri = req.getRequestURI();
            String[] uriSplit = uri.split("/");
            // 0: blank, 1: csj, 2: account, 3: customer
            if(uriSplit[3].equals("customer")) {
                switch(uriSplit[4]) {
                    case "register":
                        rd = processRegisterCustomer(req, user);
                        break;
                    case "order":
                        switch(uriSplit[5]) {
                            case "add":
                                rd = processAddCustomerOrder(req, user);
                                break;
                            case "update":
                                rd = processUpdateCustomerOrder(req, user);
                                break;
                            case "delete":
                                rd = processDeleteCustomerOrder(req, user);
                            break;
                        }
                        break;
                }
            }

            if(rd == null) {
                res.sendRedirect(req.getContextPath());
            }
            else {
                rd.forward(req, res);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            String uri = req.getRequestURI();
            String[] uriSplit = uri.split("/");
            if(uriSplit[3].equals("customer")) {
                switch(uriSplit[4]) {
                    case "register":
                        doRegisterCustomer(req, res);
                        break;
                    case "order":
                        switch(uriSplit[5]) {
                            case "add":
                                doAddOrder(req, res);
                                break;
                            case "delete":
                                doDeleteOrder(req, res);
                                break;
                        }
                }
            }
    }

    private void doRegisterCustomer(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        Status status = auth.doRegister(req, res);
        if(status == Status.OK) {
            req.getSession().setAttribute("command-executed", "sale-customer-register");
            res.sendRedirect(req.getContextPath() + "/success");
        }
        else if(status == Status.REG_USERNAME_EXIST) {
            req.setAttribute("isUsernameExist", "true");
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, res);
        }
    }

    private RequestDispatcher processRegisterCustomer(HttpServletRequest req, User loggedUser) {
        if(loggedUser.getRole() != Role.SALESMAN) {
            return null;
        }
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/register.jsp");
        return rd;
    }

    private RequestDispatcher processAddCustomerOrder(HttpServletRequest req, User loggedUser) {
        if(loggedUser.getRole() != Role.SALESMAN) {
            return null;
        }
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/customer/order_add.jsp");

        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        if(action != null) {
            switch(action) {
                case "choose-user":
                    String username = req.getParameter("username");
                    if(username != null) {
                        Authenticator auth = new Authenticator();
                        User user = auth.getUser(req.getServletContext(), username);
                        if(user == null) {
                            req.setAttribute("addFailed", "username");
                        }
                        else {
                            session.setAttribute("user-queried", user);
                        }
                    }
                break;

                case "choose-category":
                    String category = req.getParameter("category");
                    if(category != null) {
                        if(category.equals("none")) {
                            req.setAttribute("addFailed", "category");
                        }
                        else {
                            Map<Integer, Product> listProduct = pm.getListProduct(req, category);
                            req.setAttribute("listProduct", listProduct);
                            req.setAttribute("category", category);
                        }
                    }
                break;

                case "add-product":
                    category = req.getParameter("category");
                    System.out.println("add product");
                    Integer productId = Integer.valueOf(req.getParameter("product-id"));
                    Integer amount = Integer.valueOf(req.getParameter("product-amount"));
                    if(productId != null && amount != null) {
                        @SuppressWarnings("unchecked")
                        Map<Product, Integer> mapOrderProduct =
                         (Map<Product, Integer>) session.getAttribute("order-product-list");

                        if(mapOrderProduct == null) {
                            mapOrderProduct = new LinkedHashMap<>();
                            session.setAttribute("order-product-list", mapOrderProduct);
                        }

                        Map<Integer, Product> mapProduct = pm.getListProduct(req, category);
                        Product p = mapProduct.get(productId);

                        mapOrderProduct.put(p, amount);
                        session.setAttribute("order-product-list", mapOrderProduct);
                        req.setAttribute("recently-added", "true");
                    }
                break;

                case "finish-add":
                    System.out.println("done add product");
                    req.setAttribute("done-add-product", "true");
                break;
            }
        }
        return rd;
    }

    private RequestDispatcher processUpdateCustomerOrder(HttpServletRequest req, User loggedUser) {
        if(loggedUser.getRole() != Role.SALESMAN) {
            return null;
        }
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/customer/order_update.jsp");

        return rd;
    }

    private RequestDispatcher processDeleteCustomerOrder(HttpServletRequest req, User loggedUser) {
        if(loggedUser.getRole() != Role.SALESMAN) {
            return null;
        }
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/customer/order_delete.jsp");

        HttpSession session = req.getSession();
        String action = req.getParameter("action");

        if(action != null) {
            switch(action) {
                case "choose-user":
                    String username = req.getParameter("username");
                    User user = null;
                    if(username != null) {
                        Authenticator auth = new Authenticator();
                        user = auth.getUser(req.getServletContext(), username);
                        if(user == null) {
                            req.setAttribute("addFailed", "username");
                        }
                        else {
                            session.setAttribute("user-queried", user);
                        }
                    }
                    if(user != null) {
                        List<Order> listOrder = om.getListOrder(req, user);
                        session.setAttribute("list-order", listOrder);
                    }
                    break;
            }
        }

        return rd;
    }

    private void doAddOrder(HttpServletRequest req, HttpServletResponse res)
        throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user-queried");
        @SuppressWarnings("unchecked")
        LinkedHashMap<Product, Integer> mapProduct = 
            (LinkedHashMap<Product, Integer>) session.getAttribute("order-product-list");
        Order order = om.buildOrder(req, user, mapProduct);
        om.addToOrderFile(req, res, order);

        session.removeAttribute("user-queried");
        session.removeAttribute("order-product-list");

        session.setAttribute("command-executed", "sales-order-add");
        res.sendRedirect(req.getContextPath() + "/success");
        
    }

    private void doDeleteOrder(HttpServletRequest req, HttpServletResponse res)
        throws IOException {
        HttpSession session = req.getSession();
        @SuppressWarnings("unchecked")
        List<Order> listOrder = (List<Order>) session.getAttribute("list-order");

        Integer orderId = Integer.valueOf(req.getParameter("order-id"));
        Order toDelete = null;
        for(Order order: listOrder) {
            if(order.getId().equals(orderId)) {
                toDelete = order;
                break;
            }
        }

        om.deleteOrder(req, toDelete);

        session.setAttribute("command-executed", "sales-order-delete");
        res.sendRedirect(req.getContextPath() + "/success");
    }
}