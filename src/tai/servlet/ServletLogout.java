package tai.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
public class ServletLogout extends HttpServlet {

    @Override

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            req.getSession().invalidate();
            res.sendRedirect(req.getContextPath());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            doGet(req, res);
    }
}