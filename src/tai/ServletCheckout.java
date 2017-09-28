package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class ServletCheckout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");
        if(currentUser == null) {
            res.sendRedirect(req.getContextPath() + "/login");
        }
        else {
            req.getRequestDispatcher("/WEB-INF/jsp/checkout.jsp").forward(req, res);
        }
    }
}