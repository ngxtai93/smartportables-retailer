package tai.listener;

import javax.servlet.*;

import tai.model.CategoryManager;
import tai.model.DealMacthes;
import tai.model.ProductManager;
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
        pm.processLoadToMySQL(sc);
        
        DealMacthes dm = new DealMacthes();
        dm.processDealMatch(sc);
    }
}