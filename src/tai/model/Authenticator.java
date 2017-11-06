package tai.model;

import javax.servlet.*;
import javax.servlet.http.*;

import tai.constants.Status;
import tai.entity.User;
import tai.utils.MySQLDataStoreUtilities;


public class Authenticator {

    private ShoppingCartManager cm;
    private MySQLDataStoreUtilities mysqlUtil = MySQLDataStoreUtilities.INSTANCE;

    public Authenticator() {
        cm = new ShoppingCartManager();
    }

    public Status doRegister(HttpServletRequest req, HttpServletResponse res) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        ServletContext sc = req.getServletContext();
        if(getUser(sc, username) == null) {
            User registeredUser = registerCustomer(sc, username, password);

            if(req.getSession().getAttribute("currentUser") == null) {
                req.getSession().setAttribute("currentUser", registeredUser);
            }
            return Status.OK;
        }
        else {
            return Status.REG_USERNAME_EXIST;
        }
    }

    public User getUser(ServletContext sc, String username) {
        User user = mysqlUtil.getUser(sc, username);
        if(user != null) {
            user.setShoppingCart(cm.getCart(sc, user));
        }
        return user;
    }

    public User getUser(ServletContext sc, int userId) {
        User user = mysqlUtil.getUser(sc, userId);
        if(user != null) {
            user.setShoppingCart(cm.getCart(sc, user));
        }
        return user;
    }

    public Status doLogin(HttpServletRequest req, HttpServletResponse res) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        ServletContext sc = req.getServletContext();
        User userInFile = getUser(sc, username);
        if(userInFile == null) {
            return Status.LOGIN_WRONG_USERNAME;
        }

        if(!userInFile.getPassword().equals(password)) {
            return Status.LOGIN_WRONG_PASSWORD;
        }
        req.getSession().setAttribute("currentUser", userInFile);
        return Status.OK;
    }

    private User registerCustomer(ServletContext sc, String username, String password) {
        return mysqlUtil.registerCustomer(sc, username, password);
    }
}