package tai.utils;

import java.sql.*;
import java.util.Properties;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import tai.entity.Order;
import tai.entity.Role;
import tai.entity.User;
import tai.entity.Product;

public enum MySQLDataStoreUtilities {
    INSTANCE;

    private final String PROPERTIES_FILE_PATH = "resources/database.properties";

    private MySQLDataStoreUtilities() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public User getUser(ServletContext sc, String username) {
        User user = null;
        Connection conn = initConnection(sc);
        if(conn == null) {
            return null;
        }

        String sql = "select * from smart_portables.login_user WHERE username = ?";
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
        
        try {
            conn.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public User getUser(ServletContext sc, int userId) {
        User user = null;
        Connection conn = initConnection(sc);
        if(conn == null) {
            return null;
        }

        String sql = "select * from smart_portables.login_user WHERE seq_no = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
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
        
        try {
            conn.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public User registerCustomer(ServletContext sc, String username, String password) {
        Connection conn = initConnection(sc);
        if(conn == null) {
            return null;
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
        sql = "SELECT seq_no from smart_portables.login_user WHERE username = ?";
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

        try {
            conn.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }        

        User user = new User(username, password, Role.CUSTOMER);
        user.setId(id);
        return user;
    }

    public void insertOrder(ServletContext sc, Order order) {
        Connection conn = initConnection(sc);
        if(conn == null) {
            return;
        }

        String sql =    "INSERT INTO `smart_portables`.`order` "
                        + "(`user`, `order_date`, `deliver_date`"
                        + ", `confirm_number`, `name`, `address`"
                        + ", `city`, `state`, `zip`"
                        + ", `phone`, `credit_card`, `expire`"
                        + ", `status`, `product_link`)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
        ;

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, order.getUser().getId().intValue());
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

        try {
            conn.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> selectOrder(ServletContext sc, User user) {
        List<Order> listOrder = null;
        Connection conn = initConnection(sc);
        if(conn == null) {
            return null;
        }

        String sql =    "SELECT * from smart_portables.order WHERE user = ?";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user.getId().intValue());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if(rs.getRow() == 1) {
                    listOrder = new ArrayList<>();
                }

                listOrder.add(buildOrder(rs, user));
            }

            rs.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        try {
            conn.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return listOrder;
    }

    public Order selectOrder(ServletContext sc, int orderId) {
        Order order = null;
        Connection conn = initConnection(sc);
        if(conn == null) {
            return null;
        }

        String sql =    "SELECT * from smart_portables.order WHERE seq_no = ?";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                order = buildOrder(sc, rs);
            }

            rs.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        try {
            conn.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return order;
    }

    public void updateOrder(ServletContext sc, int id, Order newOrder) {
        Connection conn = initConnection(sc);
        if(conn == null) {
            return;
        }

        String sql =    "UPDATE `smart_portables`.`order`"
                        + " SET `name`= ?"
                        + ", `address`= ?"
                        + ", `city`= ?"
                        + ", `state`= ?"
                        + ", `zip`= ?"
                        + ", `phone`= ?"
                        + ", `credit_card`= ?"
                        + ", `expire`= ?"
                        + ", `deliver_date`= ?"
                        + ", `status`= ?"
                        + " WHERE `seq_no`= ?;";
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString    (1, newOrder.getName());
            ps.setString    (2, newOrder.getAddress());
            ps.setString    (3, newOrder.getCity());
            ps.setString    (4, newOrder.getState());
            ps.setInt       (5, newOrder.getZip());
            ps.setLong      (6, newOrder.getPhone());
            ps.setLong      (7, newOrder.getCreditCardNum());
            ps.setString    (8, newOrder.getShortExpDate());
            ps.setDate      (9, Date.valueOf(newOrder.getDeliverDate()));
            ps.setString    (10, newOrder.getStatus());
            ps.setInt       (11, id);
            ps.execute();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        try {
            conn.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteOrder(ServletContext sc, Order order) {
        Connection conn = initConnection(sc);
        if(conn == null) {
            return;
        }
        int id = order.getId().intValue();

        String sql = "DELETE FROM `smart_portables`.`order` WHERE `seq_no`= ?;";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        try {
            conn.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

    }

    private Order buildOrder(ResultSet rs, User user) {
        Order order = new Order();
        try {
            order.setId             (Integer.valueOf(rs.getInt("seq_no")));
            order.setUser           (user);
            order.setOrderDate      (rs.getDate("order_date").toLocalDate());
            order.setDeliverDate    (rs.getDate("deliver_date").toLocalDate());
            order.setConfirmNumber  (Long.valueOf(rs.getLong("confirm_number")));
            order.setListProduct    (buildListProduct(rs.getString("product_link")));
            order.setName           (rs.getString("name"));
            order.setAddress        (rs.getString("address"));
            order.setCity           (rs.getString("city"));
            order.setState          (rs.getString("state"));
            order.setZip            (Integer.valueOf(rs.getInt("zip")));
            order.setPhone          (Long.valueOf(rs.getLong("phone")));
            order.setCreditCardNum  (Long.valueOf(rs.getLong("credit_card")));
            order.setExpireDate     (processExpDate(rs.getString("expire")));
            order.setStatus         (rs.getString("status"));
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return order;
    }

    private Order buildOrder(ServletContext sc, ResultSet rs) {
        Order order = new Order();
        try {
            int userId = rs.getInt("user");
            User user = getUser(sc, userId);

            order.setId             (Integer.valueOf(rs.getInt("seq_no")));
            // order.setUsername       (user.getUsername());
            order.setOrderDate      (rs.getDate("order_date").toLocalDate());
            order.setDeliverDate    (rs.getDate("deliver_date").toLocalDate());
            order.setConfirmNumber  (Long.valueOf(rs.getLong("confirm_number")));
            order.setListProduct    (buildListProduct(rs.getString("product_link")));
            order.setName           (rs.getString("name"));
            order.setAddress        (rs.getString("address"));
            order.setCity           (rs.getString("city"));
            order.setState          (rs.getString("state"));
            order.setZip            (Integer.valueOf(rs.getInt("zip")));
            order.setPhone          (Long.valueOf(rs.getLong("phone")));
            order.setCreditCardNum  (Long.valueOf(rs.getLong("credit_card")));
            order.setExpireDate     (processExpDate(rs.getString("expire")));
            order.setStatus         (rs.getString("status"));
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return order;
    }

    private LinkedHashMap<Product, Integer> buildListProduct
        (String productLink) {
            LinkedHashMap<Product, Integer> result = new LinkedHashMap<>();
            // product separated by ';'
            String[] productLinkSplit = productLink.split(";");
            for(int i = 0; i < productLinkSplit.length; i++) {
                // format product: [category],[product_id],[amount]
                String[] productSplit = productLinkSplit[i].split(",");
                Product product = new Product();
                product.setCategory(productSplit[0]);
                product.setId(Integer.valueOf(productSplit[1]));
                result.put(product, Integer.valueOf(productSplit[2]));
            }

            return result;
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

    private Connection initConnection(ServletContext sc) {
        String propertiesFullFilePath = sc.getRealPath(PROPERTIES_FILE_PATH);
        Connection conn = null;
        try(InputStream input = new FileInputStream(propertiesFullFilePath)) {
            Properties prop = new Properties();
            prop.load(input);

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
        catch(SQLException | IOException e) {
            e.printStackTrace();
        }

        return conn;
    }
    
    private LocalDate processExpDate(String sqlString) {
        // format sqlString: e.g. 0719
        String monthStr = sqlString.substring(0, 2);
        String yearStr = sqlString.substring(3, 5);

        int month = Integer.parseInt(monthStr);
        int year = 2000 + Integer.parseInt(yearStr);

        LocalDate date = LocalDate.of(year, month, 1);
        return date.plusMonths(1).minusDays(1);
    }
}

