package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import tai.User;
import tai.Role;

public class ServletManageAccount extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("currentUser");
        if(user == null) {
            res.sendRedirect("login");
        }
        else {
            switch(user.getRole()) {
                case CUSTOMER:
                    req.getRequestDispatcher("/WEB-INF/jsp/account/customer.jsp").forward(req, res);
                    break;
                case STORE_MANAGER:
                    req.getRequestDispatcher("/WEB-INF/jsp/account/store_manager.jsp").forward(req, res);
                    break;
                case SALESMAN:
                    req.getRequestDispatcher("/WEB-INF/jsp/account/salesman.jsp").forward(req, res);
                    break;
            }
        }
    }
}