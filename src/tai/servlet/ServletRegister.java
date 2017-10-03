package tai.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import tai.model.Authenticator;
import tai.constants.Status;

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
                res.sendRedirect(req.getContextPath());
            }
            else if(status == Status.REG_USERNAME_EXIST) {
                req.setAttribute("isUsernameExist", "true");
                req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, res);
            }
    }
}