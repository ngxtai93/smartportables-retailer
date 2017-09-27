package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Map;
import java.util.ArrayList;

public class ServletCart extends HttpServlet {

    @Override

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");
        if(currentUser == null) {
            res.sendRedirect(req.getContextPath() + "/login");
        }
        else {
            req.getRequestDispatcher("/WEB-INF/jsp/cart.jsp").forward(req, res);    
        }
    }
    
    private ShoppingCartManager cm;
    private ProductManager pm;
    
    public ServletCart() {
        cm = new ShoppingCartManager();
        pm = new ProductManager();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            User currentUser = (User) req.getSession().getAttribute("currentUser");
            if(currentUser == null) {
                res.sendRedirect(req.getContextPath() + "/login");
            }
            else {  
                String[] uriSplit = req.getRequestURI().split("/");
                // 0: blank, 1: csj, 2: cart
                if(uriSplit.length == 4) {
                    switch(uriSplit[3]) {
                        case "add":
                            processAddToCart(req, res);
                            break;
                    }
                }
            }
    }

    private void processAddToCart(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            String category = (String) req.getParameter("category");
            Integer productId = Integer.valueOf(req.getParameter("product-id"));
            User currentUser = (User) req.getSession().getAttribute("currentUser");

            cm.addToCart(req, res, currentUser, category, productId);

            // update currentUser
            Authenticator auth = new Authenticator();
            currentUser = auth.getUser(req.getServletContext(), currentUser.getUsername());
            HttpSession session = req.getSession();
            
            session.setAttribute("currentUser", currentUser);
            session.setAttribute("command-executed", "cart-add");
            session.setAttribute("prev-uri", req.getHeader("Referer"));
            res.sendRedirect(req.getContextPath() + "/success");
    }
}