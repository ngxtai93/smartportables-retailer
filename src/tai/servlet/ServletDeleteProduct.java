package tai.servlet;

import java.io.*;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;

import tai.entity.Product;
import tai.entity.Role;
import tai.entity.User;
import tai.model.ProductManager;
import tai.utils.XmlUtilities;

public class ServletDeleteProduct extends HttpServlet {

    private XmlUtilities xmlUtil = XmlUtilities.INSTANCE;
    ProductManager pm = new ProductManager();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
        if(user.getRole() != Role.STORE_MANAGER) {
            res.sendRedirect(req.getContextPath());
        }
        else {
            // process request
            String[] listIdToDelete = req.getParameterValues("product-id");
            String category = req.getParameter("category");
            if(listIdToDelete == null || listIdToDelete.length == 0) {
                Map<Integer, Product> mapProduct = new ProductManager().getListProduct(category);
                req.setAttribute("no-product-chosen", "true");
                req.setAttribute("mapProduct", mapProduct);
                req.getRequestDispatcher("/WEB-INF/jsp/product/product_delete.jsp").forward(req, res);
            }
            else {
            	for(int i = 0; i < listIdToDelete.length; i++) {
            		pm.deleteProduct(category, Integer.parseInt(listIdToDelete[i]));
            	}
                
            
                req.getSession().setAttribute("command-executed", "product-delete");
                res.sendRedirect(req.getContextPath() + "/success");
            }
            
        }
    }
}