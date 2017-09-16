package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import tai.Authenticator;
import tai.Status;
public class ServletLogin extends HttpServlet {

    private Authenticator auth;

    public ServletLogin() {
        auth = new Authenticator();
    }
    @Override

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            Status status = auth.doLogin(req, res);
            if(status == Status.LOGIN_WRONG_PASSWORD) {
                req.setAttribute("loginFailed", "password");
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, res);
            }
            else if(status == Status.LOGIN_WRONG_USERNAME) {
                req.setAttribute("loginFailed", "username");
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, res);
            }
            else if(status == Status.OK) {
                req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, res);
            }
            
    }
}