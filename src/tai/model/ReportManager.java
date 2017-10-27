package tai.model;

import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

import tai.entity.Category;
import tai.entity.Product;

public class ReportManager {
    private ProductManager pm;

    public ReportManager() {
        pm = new ProductManager();
    }

    public List<Product> getListAllProduct(HttpServletRequest req) {
        return pm.getListProduct(req.getSession().getServletContext());
    }
}