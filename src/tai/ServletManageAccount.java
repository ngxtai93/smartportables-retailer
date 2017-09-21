package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Map;

public class ServletManageAccount extends HttpServlet {

    private ProductManager pm;

    public ServletManageAccount() {
        pm = new ProductManager();
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
            // 0: blank, 1: csj, 2: account
            if(uriSplit.length == 3) {
                rd = loadAccountInfo(req, user);
            }
            else if(uriSplit[3].equals("product")) {
                switch(uriSplit[4]) {
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

            if(rd == null) {
                res.sendRedirect(req.getContextPath());
            }
            else {
                rd.forward(req, res);
            }
        }
    }
    

    private RequestDispatcher loadAccountInfo(HttpServletRequest req, User loggedUser) {
        RequestDispatcher rd = null;
        switch(loggedUser.getRole()) {
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
        if(loggedUser.getRole() != Role.STORE_MANAGER) {
            return null;
        }
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/product/product_add.jsp");
        return rd;
    }

    
    private RequestDispatcher processProductUpdate(HttpServletRequest req, User loggedUser) {
        if(loggedUser.getRole() != Role.STORE_MANAGER) {
            return null;
        }
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/product/product_update.jsp");

        if(req.getQueryString() != null) {
            String[] queryStringSplit = req.getQueryString().split("=");
            for(int i = 0; i < queryStringSplit.length - 1; i = i + 2) {
                String name = queryStringSplit[i];
                String value = queryStringSplit[i + 1];

                switch(name) {
                    case "category":
                        Map<Integer, Product> mapProduct = pm.getListProduct(req, value);
                        req.setAttribute("mapProduct", mapProduct);
                        break;
                }
            }
        }
        return rd;
    }

    private RequestDispatcher processProductDelete(HttpServletRequest req, User loggedUser) {
        if(loggedUser.getRole() != Role.STORE_MANAGER) {
            return null;
        }
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/product/product_delete.jsp");

        if(req.getQueryString() != null) {
            String[] queryStringSplit = req.getQueryString().split("=");
            for(int i = 0; i < queryStringSplit.length - 1; i = i + 2) {
                String name = queryStringSplit[i];
                String value = queryStringSplit[i + 1];

                switch(name) {
                    case "category":
                        Map<Integer, Product> mapProduct = pm.getListProduct(req, value);
                        req.setAttribute("mapProduct", mapProduct);
                        break;
                }
            }
        }
        return rd;
    }
}