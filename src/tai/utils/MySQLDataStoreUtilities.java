package tai.utils;

import java.sql.*;
import java.util.Properties;
import java.util.Map;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import tai.entity.Order;
import tai.entity.Role;
import tai.entity.User;
import tai.entity.Product;

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
                user.setId(rs.getInt("seq_no"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                switch(rs.getString("type")) {
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

        // get id of new account
        sql = "SELECT seq_no from login_user where username = ?";
        Integer id = null;
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            rs.next();
            id = Integer.valueOf(rs.getInt("seq_no"));
        }
        catch(SQLException e) {
            e.printStackTrace();
        }


        User user = new User(username, password, Role.CUSTOMER);
        user.setId(id);
        return user;
    }

    public void insertOrder(ServletContext sc, Order order, User user) {
        if(conn == null) {
            initConnection(sc);
        }

        String sql =    "INSERT INTO `smart_portables`.`order` "
                        + "(`user`, `order_date`, `deliver_date`"
                        + ", `confirm_number`, `name`, `address`"
                        + ", `city`, `state`, `zip`"
                        + ", `phone`, `creditcard`, `expire`"
                        + ", `status`, `product_link`)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
        ;

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, user.getId().intValue());
            ps.setDate  (2, Date.valueOf(order.getOrderDate()));
            ps.setDate  (3, Date.valueOf(order.getDeliverDate()));
            ps.setLong  (4, order.getConfirmNumber().longValue());
            ps.setString(5, order.getName());
            ps.setString(6, order.getAddress());
            ps.setString(7, order.getCity());
            ps.setString(8, order.getState());
            ps.setInt   (9, order.getZip().intValue());
            ps.setLong  (10, order.getPhone().longValue());
            ps.setLong  (11, order.getCreditCardNum().longValue());
            ps.setString(12, order.getShortExpDate());
            ps.setString(13, order.getStatus());
            ps.setString(14, buildProductLink(order));
            ps.execute();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

    }

    private String buildProductLink(Order order) {
        StringBuilder sb = new StringBuilder();
        Map<Product, Integer> mapProduct = order.getListProduct();

        for(Map.Entry<Product, Integer> entry: mapProduct.entrySet()) {
            Product p = entry.getKey();
            Integer amount = entry.getValue();

            sb  .append(p.getCategory())
                .append(",")
                .append(p.getId())
                .append(",")
                .append(String.valueOf(amount))
                .append(";")
            ;
        }

        return sb.toString();
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

