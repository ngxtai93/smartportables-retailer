package tai;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import tai.Status;
import tai.User;
public class Authenticator {

    private final String USER_INFO_PATH = "resources/data/user/";
    private final String CUSTOMER_FILE_NAME = "customer.txt";
    private final String SALE_FILE_NAME = "salesman.txt";
    private final String SM_FILE_NAME = "storemanager.txt";
    public Status doRegister(HttpServletRequest req, HttpServletResponse res) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        ServletContext sc = req.getServletContext();
        if(!isUserExists(sc, username)) {
            registerCustomer(sc, username, password);
            return Status.OK;
        }
        else {
            return Status.REG_USERNAME_EXIST;
        }
    }

    private boolean isUserExists(ServletContext sc, String username) {
        User user = null;
        if((user = findCustomerById(sc, username)) != null) {
            return true;
        }
        if((user = findSalesmanById(sc, username)) != null) {
            return true;
        }
        if((user = findStoreManagerById(sc, username)) != null) {
            return true;
        }
        return false;
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