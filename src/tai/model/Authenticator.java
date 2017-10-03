package tai.model;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import tai.entity.User;
import tai.entity.Role;
import tai.model.ShoppingCartManager;
import tai.utils.MySQLDataStoreUtilities;
import tai.constants.Status;


public class Authenticator {

    private final String USER_INFO_PATH = "resources/data/user/";
    private final String CUSTOMER_FILE_NAME = "customer.txt";
    private final String SALE_FILE_NAME = "salesman.txt";
    private final String SM_FILE_NAME = "storemanager.txt";
    
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
            registerCustomer(sc, username, password);

            User registeredUser = new User(username, password, Role.CUSTOMER);
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
        return mysqlUtil.getUser(sc, username);
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

    private void registerCustomer(ServletContext sc, String username, String password) {
        String toAppend = username + "\t" + password;
        File file = new File(sc.getRealPath(USER_INFO_PATH + CUSTOMER_FILE_NAME));
        try(
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));  // append = true
        ) {
            out.println(toAppend);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}