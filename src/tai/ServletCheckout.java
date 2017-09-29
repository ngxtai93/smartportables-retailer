package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class ServletCheckout extends HttpServlet {

    private OrderManager om;
    
    public ServletCheckout() {
        om = new OrderManager();
    }
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");
        if(currentUser == null) {
            res.sendRedirect(req.getContextPath() + "/login");
        }

        Order order = om.processOrderPlaced(req, res);

        req.getSession().setAttribute("command-executed", "order-place");
        req.getSession().setAttribute("order", order);
        res.sendRedirect(req.getContextPath() + "/success");
    }

}