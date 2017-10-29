package tai.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.time.LocalDate;

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

    public Map<LocalDate, Double> buildMapSaleByDay(HttpServletRequest req) {
        Map<LocalDate, Double> result = new LinkedHashMap<>();
        List<Order> listOrder = om.getListOrder(req, null);

        // start with current date, decrease date by 1 until order list is empty
        LocalDate date = LocalDate.now();
        
        while(listOrder.size() > 0) {
            for(Iterator<Order> itr = listOrder.iterator(); itr.hasNext(); ){
                Order o = itr.next();
                if(o.getOrderDate().compareTo(date) == 0) {
                    double totalSale = 0.0;
                    Map<Product, Integer> mapProduct = o.getListProduct();
                    for(Map.Entry<Product, Integer> entry: mapProduct.entrySet()) {
                        totalSale += (entry.getKey().getPrice().doubleValue() * entry.getValue());
                    }

                    // if already in map, add into existing sale, else create new
                    if(result.containsKey(date)) {
                        result.put(date, Double.valueOf(result.get(date).doubleValue() + totalSale));
                    }
                    else {
                        result.put(date, Double.valueOf(totalSale));
                    }
                    itr.remove();
                }
            }
            date = date.minusDays(1);
        }

        return result;
    }
}