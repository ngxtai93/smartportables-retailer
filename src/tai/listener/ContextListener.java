package tai.listener;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import tai.utils.MongoDBDataStoreUtilities;
import tai.utils.MySQLDataStoreUtilities;

public class ContextListener implements ServletContextListener {
    private MongoDBDataStoreUtilities mongoDbUtil = MongoDBDataStoreUtilities.INSTANCE;
    private MySQLDataStoreUtilities mySqlUtil = MySQLDataStoreUtilities.INSTANCE;

    @Override
    public void contextDestroyed(ServletContextEvent e) {
        mongoDbUtil.closeMongoDbConnection();
        mySqlUtil.closeConnection();
    }

    @Override
    public void contextInitialized(ServletContextEvent e) {
        mongoDbUtil.initMongoDbConnection();
        mySqlUtil.initConnection(e.getServletContext());
    }
}