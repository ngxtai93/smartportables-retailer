package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import tai.Constants;
public class Authenticator {
    public Constants doRegister(HttpServletRequest req, HttpServletResponse res) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        
        return Constants.OK;
    }
}