package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class ServletAddProduct extends HttpServlet {

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