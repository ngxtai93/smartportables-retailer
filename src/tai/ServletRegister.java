package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import tai.Authenticator;
import tai.Status;
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
            Status status = auth.doRegister(req, res);
            if(status == Status.OK) {
                req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, res);
            }
            else if(status == Status.REG_USERNAME_EXIST) {
                
            }
    }
}