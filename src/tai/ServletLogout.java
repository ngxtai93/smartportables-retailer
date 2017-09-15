package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
public class ServletLogout extends HttpServlet {

    @Override

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            req.removeAttribute("username");
            req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            doGet(req, res);
    }
}