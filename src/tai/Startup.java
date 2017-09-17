package tai;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Startup extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, res);
        }
}