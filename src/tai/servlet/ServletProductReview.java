package tai.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import tai.entity.User;

public class ServletProductReview extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
            User currentUser = (User) req.getSession().getAttribute("currentUser");
            if(currentUser == null) {
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            } 

            req.getRequestDispatcher("/WEB-INF/jsp/product/product_review.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException {
        
    }
}