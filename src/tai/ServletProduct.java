package tai;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Map;

public class ServletProduct extends HttpServlet {
    private ProductManager pm;
    private StringUtilities stringUtil = StringUtilities.INSTANCE;

    public ServletProduct() {
        pm = new ProductManager();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        String uri = req.getRequestURI();
        String[] uriSplit = uri.split("/");
        // 0: blank, 1: csj, 2: product, 3: [category]
        
        Map<Integer, Product> mapProduct = pm.getListProduct(req, uriSplit[3]);

        if(uriSplit.length == 4) {
            req.setAttribute("current-category", uriSplit[3]);
            req.setAttribute("mapProduct", mapProduct);
            req.getRequestDispatcher("/WEB-INF/jsp/product/product_browse.jsp").forward(req, res);
        }
        else if(uriSplit.length == 5) {
            if(stringUtil.isNumeric(uriSplit[4])) {
                Integer productId = Integer.valueOf(uriSplit[4]);
                Product product = mapProduct.get(productId);
                if(product == null) {
                    res.sendRedirect(req.getContextPath());
                }
                else {
                    req.setAttribute("current-product", product);
                    req.getRequestDispatcher("/WEB-INF/jsp/product/product_detail.jsp").forward(req, res);
                }
            }
            else {
                res.sendRedirect(req.getContextPath());
            }
            
        }
}
}