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
                loadAccountInfo(req, res, user);
            }
            else if(uriSplit[3].equals("product")) {
                switch(uriSplit[4]) {
                    case "add":
                        rd = req.getRequestDispatcher("/WEB-INF/jsp/product/product_add.jsp");
                    break;
                }
                rd.forward(req, res);
            }
            else {
                res.sendRedirect(req.getContextPath());
            }
        }
    }

    private void loadAccountInfo(HttpServletRequest req, HttpServletResponse res, User loggedUser)
        throws ServletException, IOException {
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
        rd.forward(req, res);
    }
}