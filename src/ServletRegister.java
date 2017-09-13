import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class ServletRegister extends HttpServlet {

    @Override

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            PrintWriter out = res.getWriter();
            out.println("Called Register");
    }
}