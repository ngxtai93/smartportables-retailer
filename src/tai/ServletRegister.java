package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import tai.Authenticator;
import tai.Constants;
public class ServletRegister extends HttpServlet {

    private Authenticator auth;
    public ServletRegister() {
        auth = new Authenticator();
    }

    @Override

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            
            if(auth.doRegister(req, res) == Constants.OK) {
                req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, res);
            }
    }
}