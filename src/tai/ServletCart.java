package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class ServletCart extends HttpServlet {

    // @Override

    // protected void doGet(HttpServletRequest req, HttpServletResponse res)
    //     throws ServletException, IOException {
    //         req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, res);
    // }
    
    private ShoppingCartManager cm;
    
    public ServletCart() {
        cm = new ShoppingCartManager();
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

            System.out.println("Add to cart: category " + category + ", product: " + productId);

            res.sendRedirect(req.getContextPath());
    }
}