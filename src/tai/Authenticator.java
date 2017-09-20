package tai;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class Authenticator {

    private final String USER_INFO_PATH = "resources/data/user/";
    private final String CUSTOMER_FILE_NAME = "customer.txt";
    private final String SALE_FILE_NAME = "salesman.txt";
    private final String SM_FILE_NAME = "storemanager.txt";
    
    public Status doRegister(HttpServletRequest req, HttpServletResponse res) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        ServletContext sc = req.getServletContext();
        if(getUser(sc, username) == null) {
            registerCustomer(sc, username, password);

            User registeredUser = new User(username, password, Role.CUSTOMER);
            req.getSession().setAttribute("currentUser", registeredUser);
            return Status.OK;
        }
        else {
            return Status.REG_USERNAME_EXIST;
        }
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


    private User getUser(ServletContext sc, String username) {
        User user = null;
        for(Role r: Role.values()) {
            user = findUserByRole(sc, username, r);
            if(user != null) {
                break;
            }
        }
        return user;
    }

    private User findUserByRole(ServletContext sc, String username, Role r) {
        User user = null;
        switch(r) {
            case CUSTOMER:
                user = findCustomerById(sc, username);
                break;
            case SALESMAN:
                user = findSalesmanById(sc, username);
                break;
            case STORE_MANAGER:
                user = findStoreManagerById(sc, username);
                break;
        }
        return user;
    }
    
    private User findCustomerById(ServletContext sc, String username) {
        File file = new File(sc.getRealPath(USER_INFO_PATH + CUSTOMER_FILE_NAME));
        User result = null;
        try(
            BufferedReader br = new BufferedReader(new FileReader(file));
        ) {
            String line;
            while((line = br.readLine()) != null) {
                String[] split = line.split("\t");
                if(username.equals(split[0])) {     // found
                    result = new User(username, split[1], Role.CUSTOMER);
                    break;
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private User findSalesmanById(ServletContext sc, String username) {
        File file = new File(sc.getRealPath(USER_INFO_PATH + SALE_FILE_NAME));
        User result = null;
        try(
            BufferedReader br = new BufferedReader(new FileReader(file));
        ) {
            
            String line;
            while((line = br.readLine()) != null) {
                String[] split = line.split("\t");
                if(username.equals(split[0])) {     // found
                    result = new User(username, split[1], Role.SALESMAN);
                    break;
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private User findStoreManagerById(ServletContext sc, String username) {
        File file = new File(sc.getRealPath(USER_INFO_PATH + SM_FILE_NAME));
        User result = null;
        try(
            BufferedReader br = new BufferedReader(new FileReader(file));
        ) {
            String line;
            while((line = br.readLine()) != null) {
                String[] split = line.split("\t");
                if(username.equals(split[0])) {     // found
                    result = new User(username, split[1], Role.STORE_MANAGER);
                    break;
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return result;
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