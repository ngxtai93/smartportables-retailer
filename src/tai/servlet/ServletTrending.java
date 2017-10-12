package tai.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import tai.utils.MongoDBDataStoreUtilities;

public class ServletTrending extends HttpServlet {

    private MongoDBDataStoreUtilities mongoDbUtil = MongoDBDataStoreUtilities.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        Map<String, Double> topFiveProductByRating = getTopFiveProductByRating();
        Map<Integer, Integer> topFiveZip = getTopZipByProductCount();
        Map<String, Integer> topFiveProductByAmount = getTopFiveProductByAmount();
        
        req.setAttribute("top-five-product-rating", topFiveProductByRating);
        req.setAttribute("top-five-zip", topFiveZip);
        req.setAttribute("top-five-product-amount", topFiveProductByAmount);

        req.getRequestDispatcher("/WEB-INF/jsp/trending.jsp").forward(req, res);
    }

    private Map<String, Double> getTopFiveProductByRating() {
        Map<String, Double> result = mongoDbUtil.selectTopProductByRating(5);
        return result;
    }

    private Map<Integer, Integer> getTopZipByProductCount() {
        Map<Integer, Integer> result = mongoDbUtil.selectTopZipByProductCount(5);
        return result;
    }

    private Map<String, Integer> getTopFiveProductByAmount() {
        Map<String, Integer> result = mongoDbUtil.selectTopProductByAmount(5);
        
        return result;
    }
}