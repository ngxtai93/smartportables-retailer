package tai.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import tai.entity.Category;
import tai.entity.Order;
import tai.entity.Product;

public class ReportManager {
    private ProductManager pm;
    private OrderManager om;

    public ReportManager() {
        pm = new ProductManager();
        om = new OrderManager();
    }

    public List<Product> getListAllProduct(HttpServletRequest req) {
        return pm.getListProduct(req.getSession().getServletContext());
    }

    public Map<Product, Integer> buildMapProductAmount(HttpServletRequest req) {
        Map<Product, Integer> result = new HashMap<>();
        List<Order> listOrder = om.getListOrder(req, null);

        for(Order o: listOrder) {
            Map<Product, Integer> mapProduct = o.getListProduct();
            for(Map.Entry<Product, Integer> entry: mapProduct.entrySet()) {
                Product p = entry.getKey();
                if(result.containsKey(p)) {
                    Integer newAmount = result.get(p) + entry.getValue();
                    result.put(p, newAmount);
                }
                else {
                    result.put(p, entry.getValue());
                }
            }
        }

        return result;
    }
}