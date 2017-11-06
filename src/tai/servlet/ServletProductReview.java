package tai.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import tai.entity.Review;
import tai.entity.User;
import tai.model.ReviewManager;

import tai.utils.MongoDBDataStoreUtilities;

public class ServletProductReview extends HttpServlet {
    
    private MongoDBDataStoreUtilities mongoDbUtil = MongoDBDataStoreUtilities.INSTANCE;
    ReviewManager rm = new ReviewManager();
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
        doSubmitReview(req, res);
        
        req.getSession().setAttribute("command-executed", "product-review");
        req.getRequestDispatcher("/WEB-INF/jsp/success.jsp").forward(req, res);
    }

    private void doSubmitReview(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException {
        Review review = rm.buildReview(req);

        mongoDbUtil.insertReview(review);
    }
}