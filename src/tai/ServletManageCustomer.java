package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Map;

public class ServletManageCustomer extends HttpServlet {

    private Authenticator auth;

    public ServletManageCustomer() {
        auth = new Authenticator();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
        if(user == null) {
            res.sendRedirect(req.getContextPath() + "/login");
        }
        else {
            RequestDispatcher rd = null;
            String uri = req.getRequestURI();
            String[] uriSplit = uri.split("/");
            // 0: blank, 1: csj, 2: account, 3: customer
            if(uriSplit[3].equals("customer")) {
                switch(uriSplit[4]) {
                    case "register":
                        rd = processRegisterCustomer(req, user);
                        break;
                }
            }

            if(rd == null) {
                res.sendRedirect(req.getContextPath());
            }
            else {
                rd.forward(req, res);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            String uri = req.getRequestURI();
            String[] uriSplit = uri.split("/");
            if(uriSplit[3].equals("customer")) {
                switch(uriSplit[4]) {
                    case "register":
                        doRegisterCustomer(req, res);
                        break;
                }
            }
    }

    private void doRegisterCustomer(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        Status status = auth.doRegister(req, res);
        if(status == Status.OK) {
            req.getSession().setAttribute("command-executed", "sale-customer-register");
            res.sendRedirect(req.getContextPath() + "/success");
        }
        else if(status == Status.REG_USERNAME_EXIST) {
            req.setAttribute("isUsernameExist", "true");
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, res);
        }
    }

    private RequestDispatcher processRegisterCustomer(HttpServletRequest req, User loggedUser) {
        if(loggedUser.getRole() != Role.SALESMAN) {
            return null;
        }
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/register.jsp");
        return rd;
    }
}