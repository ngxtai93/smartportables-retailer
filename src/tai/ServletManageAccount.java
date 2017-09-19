package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import tai.User;
import tai.Role;

public class ServletManageAccount extends HttpServlet {

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
            System.out.println(uri);
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

        if(req.getQueryString() != null) {
            System.out.println("Query string: " + req.getQueryString());
        }
        else {
            System.out.println("Query string = null");
        }
        return rd;
    }
}