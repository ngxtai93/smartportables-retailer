package tai.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Map;
import java.util.List;

import tai.entity.*;
import tai.model.*;


public class ServletManageAccount extends HttpServlet {

    private ProductManager pm;
    private OrderManager om;

    public ServletManageAccount() {
        pm = new ProductManager();
        om = new OrderManager();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
     throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login");
        }
        else {
            RequestDispatcher rd = null;
            String uri = req.getRequestURI();
            String[] uriSplit = uri.split("/");
            // 0: blank, 1: csj, 2: account
            if (uriSplit.length == 3) {
                rd = loadAccountInfo(req, user);
            }
            else if (uriSplit[3].equals("product")) {
                switch (uriSplit[4]) {
                case "add":
                    rd = processProductAdd(req, user);
                    break;
                case "update":
                    rd = processProductUpdate(req, user);
                    break;
                case "delete":
                    rd = processProductDelete(req, user);
                    break;
                }
            }
            else if (uriSplit[3].equals("order")) {
                if (uriSplit.length == 4) {
                    List<Order> listOrder = om.getListOrder(req, user);
                    if (listOrder != null && listOrder.size() > 0) {
                        req.setAttribute("list-order", listOrder);
                    }
                    rd = req.getRequestDispatcher("/WEB-INF/jsp/account/order.jsp");
                }
            }

            if (rd == null) {
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
        User user = (User) req.getSession().getAttribute("currentUser");
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login");
        } else {
            String uri = req.getRequestURI();
            String[] uriSplit = uri.split("/");
            // 0: blank, 1: csj, 2: account, 3: order
            if (uriSplit.length == 5) {
                if (uriSplit[4].equals("cancel")) {
                    doCancelOrder(req, res, user);
                }
            }
        }
    }

    private RequestDispatcher loadAccountInfo(HttpServletRequest req, User loggedUser) {
        RequestDispatcher rd = null;
        switch (loggedUser.getRole()) {
        case CUSTOMER:
            rd = req.getRequestDispatcher("/WEB-INF/jsp/account/customer.jsp");
            break;
        case STORE_MANAGER:
            rd = req.getRequestDispatcher("/WEB-INF/jsp/account/store_manager.jsp");
            break;
        case SALESMAN:
            rd = req.getRequestDispatcher("/WEB-INF/jsp/account/salesman.jsp");
            break;
        }
        return rd;
    }

    private RequestDispatcher processProductAdd(HttpServletRequest req, User loggedUser) {
        if (loggedUser.getRole() != Role.STORE_MANAGER) {
            return null;
        }
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/product/product_add.jsp");

        String category = req.getParameter("category");
        if (category != null) {
            if (!category.equals("accessory")) {
                Map<Integer, Product> mapAccessory = pm.getListProduct(req, "accessory");
                req.setAttribute("list-accessory", mapAccessory);
            }
        }
        return rd;
    }

    private RequestDispatcher processProductUpdate(HttpServletRequest req, User loggedUser) {
        if (loggedUser.getRole() != Role.STORE_MANAGER) {
            return null;
        }
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/product/product_update.jsp");

        if (req.getQueryString() != null) {
            String[] queryStringSplit = req.getQueryString().split("=");
            for (int i = 0; i < queryStringSplit.length - 1; i = i + 2) {
                String name = queryStringSplit[i];
                String value = queryStringSplit[i + 1];

                switch (name) {
                case "category":
                    Map<Integer, Product> mapProduct = pm.getListProduct(req, value);
                    req.setAttribute("mapProduct", mapProduct);

                    if (!name.equals("accessory")) {
                        Map<Integer, Product> mapAccessory = pm.getListProduct(req, "accessory");
                        req.setAttribute("list-accessory", mapAccessory);
                    }
                    break;
                }
            }
        }
        return rd;
    }

    private RequestDispatcher processProductDelete(HttpServletRequest req, User loggedUser) {
        if (loggedUser.getRole() != Role.STORE_MANAGER) {
            return null;
        }
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/product/product_delete.jsp");

        if (req.getQueryString() != null) {
            String[] queryStringSplit = req.getQueryString().split("=");
            for (int i = 0; i < queryStringSplit.length - 1; i = i + 2) {
                String name = queryStringSplit[i];
                String value = queryStringSplit[i + 1];

                switch (name) {
                case "category":
                    Map<Integer, Product> mapProduct = pm.getListProduct(req, value);
                    req.setAttribute("mapProduct", mapProduct);
                    break;
                }
            }
        }
        return rd;
    }

    private void doCancelOrder(HttpServletRequest req, HttpServletResponse res, User requestedUser)
        throws IOException {
        HttpSession session = req.getSession();
        Integer id = Integer.valueOf(req.getParameter("order-id"));

        if (om.canCancel(req, id, requestedUser)) {
            om.cancelOrder(req, id);
            session.setAttribute("command-executed", "order-cancel");
            res.sendRedirect(req.getContextPath() + "/success");
        } else {
            session.setAttribute("command-executed", "order-cancel");
            res.sendRedirect(req.getContextPath() + "/error");
        }
    }
}