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
        Map<Integer, Integer> topFiveZip = getTopFiveZip();
        List<String> topFiveProductByAmount = getTopFiveProductByAmount();
        
        req.setAttribute("top-five-product-rating", topFiveProductByRating);
        req.setAttribute("top-five-zip", topFiveZip);
        req.setAttribute("top-five-product-amount", topFiveProductByAmount);

        req.getRequestDispatcher("/WEB-INF/jsp/trending.jsp").forward(req, res);
    }

    private Map<String, Double> getTopFiveProductByRating() {
        Map<String, Double> result = mongoDbUtil.selectTopProductByRating(5);
        return result;
    }

    private Map<Integer, Integer> getTopFiveZip() {
        Map<Integer, Integer> result = null;
        
        return result;
    }

    private List<String> getTopFiveProductByAmount() {
        List<String> result = new ArrayList<>();
        
        return result;
    }
}