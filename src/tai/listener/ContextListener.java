package tai.listener;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import tai.model.CategoryManager;
import tai.model.ProductManager;
import tai.entity.Product;
import tai.utils.MongoDBDataStoreUtilities;
import tai.utils.MySQLDataStoreUtilities;

public class ContextListener implements ServletContextListener {
    private MongoDBDataStoreUtilities mongoDbUtil = MongoDBDataStoreUtilities.INSTANCE;
    private MySQLDataStoreUtilities mySqlUtil = MySQLDataStoreUtilities.INSTANCE;
    private ProductManager pm = new ProductManager();
    private CategoryManager cm = new CategoryManager();

    @Override
    public void contextDestroyed(ServletContextEvent e) {
        mongoDbUtil.closeMongoDbConnection();
        mySqlUtil.closeConnection();
    }

    @Override
    /**
     * On initialization, open DB connection, and populate MySQL product table from XML
     */
    public void contextInitialized(ServletContextEvent e) {
        ServletContext sc = e.getServletContext();

        mongoDbUtil.initMongoDbConnection();
        mySqlUtil.initConnection(sc);

        cm.processLoadToMySQL(sc);

        List<Product> listProduct = pm.getListProduct(sc);
    }
}