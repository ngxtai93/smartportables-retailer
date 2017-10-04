package tai.utils;

import java.sql.*;
import java.util.Properties;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import tai.entity.Role;
import tai.entity.User;

public enum MySQLDataStoreUtilities {
    INSTANCE;

    private Connection conn;
    private final String PROPERTIES_FILE_PATH = "resources/database.properties";

    private MySQLDataStoreUtilities() {

    }

    public User getUser(ServletContext sc, String username) {
        User user = null;
        if(conn == null) {
            initConnection(sc);
        }

        String sql = "select * from login_user where username = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                switch(rs.getString("role")) {
                    case "customer":
                        user.setRole(Role.CUSTOMER);
                    break;
                    case "sales":
                        user.setRole(Role.SALESMAN);
                    break;
                    case "store_manager":
                        user.setRole(Role.STORE_MANAGER);
                    break;
                }
            }

            rs.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        

        return user;
    }

    public User registerCustomer(ServletContext sc, String username, String password) {
        if(conn == null) {
            initConnection(sc);
        }

        String sql = "INSERT INTO `smart_portables`.`login_user` (`username`, `password`, `type`) VALUES (?, ?, ?);";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, "customer");
            ps.execute();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        User user = new User(username, password, Role.CUSTOMER);
        return user;
    }

    private void initConnection(ServletContext sc) {
        String propertiesFullFilePath = sc.getRealPath(PROPERTIES_FILE_PATH);

        try(InputStream input = new FileInputStream(propertiesFullFilePath)) {
            Properties prop = new Properties();
            prop.load(input);
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            conn = DriverManager.getConnection((
                    "jdbc:mysql://"
                +   prop.getProperty("database_url")
                +   ":"
                +   prop.getProperty("port")
                +   "/" + prop.getProperty("schema")
                +   "?useSSL=false"
                ), prop.getProperty("dbuser"), prop.getProperty("password")
            );
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}

