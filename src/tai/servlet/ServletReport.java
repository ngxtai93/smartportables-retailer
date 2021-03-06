package tai.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import tai.entity.User;
import tai.entity.Product;
import tai.model.ReportManager;

public class ServletReport extends HttpServlet {

    private ReportManager rm = new ReportManager();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");
        if(currentUser == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String uri = req.getRequestURI();
        String[] uriSplit = uri.split("/");
        // 0: blank, 1: csj, 2: account, 3: report
        switch(uriSplit[4]) {
            case "inventory":
                processInventoryPage(req, res);
                break;
            case "sales":
                processSalesPage(req, res);
                break;                
        }
    }

    private void processInventoryPage(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        String uri = req.getRequestURI();
        String[] uriSplit = uri.split("/");
        // 0: blank, 1: csj, 2: account, 3: report, 4: inventory
        if(uriSplit.length == 5) {
            // display menu
            req.getRequestDispatcher("/WEB-INF/jsp/report/inventory.jsp").forward(req, res);
            return;    
        }
        else {
            switch(uriSplit[5]) {
                case "list":
                {
                    List<Product> listAllProduct = rm.getListAllProduct(req);
                    req.setAttribute("listAllProduct", listAllProduct);
                    req.getRequestDispatcher("/WEB-INF/jsp/report/inventory_list.jsp").forward(req, res);
                }
                break;
                case "barchart":
                {
                    List<Product> listAllProduct = rm.getListAllProduct(req);
                    req.setAttribute("listAllProduct", listAllProduct);
                    req.setAttribute("useBarchart", Boolean.TRUE);
                    req.getRequestDispatcher("/WEB-INF/jsp/report/inventory_barchart.jsp").forward(req, res);
                }
                break;
                case "onsale":
                {
                    List<Product> listAllProduct = rm.getListAllProduct(req);
                    req.setAttribute("listAllProduct", listAllProduct);
                    req.getRequestDispatcher("/WEB-INF/jsp/report/inventory_onsale.jsp").forward(req, res);
                }
                break;
                case "rebate":
                {
                    List<Product> listAllProduct = rm.getListAllProduct(req);
                    req.setAttribute("listAllProduct", listAllProduct);
                    req.getRequestDispatcher("/WEB-INF/jsp/report/inventory_rebate.jsp").forward(req, res);
                }
                break;
            }
        }
    }

    private void processSalesPage(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        String uri = req.getRequestURI();
        String[] uriSplit = uri.split("/");
        // 0: blank, 1: csj, 2: account, 3: report, 4: sales
        if(uriSplit.length == 5) {
            // display menu
            req.getRequestDispatcher("/WEB-INF/jsp/report/sales.jsp").forward(req, res);
            return;
        }
        else {
            switch(uriSplit[5]) {
                case "list":
                {
                    Map<Product, Integer> mapProductAmount = rm.buildMapProductAmount(req);
                    req.setAttribute("mapProductAmount", mapProductAmount);
                    req.getRequestDispatcher("/WEB-INF/jsp/report/sales_list.jsp").forward(req, res);
                }
                break;
                case "barchart":
                {
                    Map<Product, Integer> mapProductAmount = rm.buildMapProductAmount(req);
                    req.setAttribute("mapProductAmount", mapProductAmount);
                    req.setAttribute("useBarchart", Boolean.TRUE);
                    req.getRequestDispatcher("/WEB-INF/jsp/report/sales_barchart.jsp").forward(req, res);
                }
                break;
                case "daily":
                {
                    Map<LocalDate, Double> mapSaleByDay = rm.buildMapSaleByDay(req);
                    req.setAttribute("mapSaleByDay", mapSaleByDay);
                    req.getRequestDispatcher("/WEB-INF/jsp/report/sales_daily.jsp").forward(req, res);
                }
                break;
            }
        }
    }
}