package tai.listener;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import tai.utils.MongoDBDataStoreUtilities;

public class ContextListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent e) {
        MongoDBDataStoreUtilities.INSTANCE.closeMongoDbConnection();
    }

    @Override
    public void contextInitialized(ServletContextEvent e) {
        
    }
}