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

import tai.entity.Category;
import tai.entity.Order;
import tai.entity.Role;
import tai.entity.User;
import tai.entity.Product;

public enum MySQLDataStoreUtilities {
    INSTANCE;

    private final String PROPERTIES_FILE_PATH = "resources/database.properties";
    private Connection conn;

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
        


        return user;
    }

    public User getUser(ServletContext sc, int userId) {
        User user = null;
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
        


        return user;
    }

    public User registerCustomer(ServletContext sc, String username, String password) {
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

        

        User user = new User(username, password, Role.CUSTOMER);
        user.setId(id);
        return user;
    }

    public void insertOrder(ServletContext sc, Order order) {
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


    }

    public List<Order> selectOrder(ServletContext sc, User user) {
        List<Order> listOrder = null;
       // if user is null, select all order
        String sql = null;
        if(user != null) {
            sql =   "SELECT * from smart_portables.order WHERE user = ?";
        }
        else {
            sql =   "SELECT * from smart_portables.order";
        }

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            if(user != null) {
                ps.setInt(1, user.getId().intValue());
            }
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if(rs.getRow() == 1) {
                    listOrder = new ArrayList<>();
                }

                if(user != null) {
                    listOrder.add(buildOrder(rs, user));
                }
                else {
                    listOrder.add(buildOrder(sc, rs));
                }
            }

            rs.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }



        return listOrder;
    }

    public Order selectOrder(ServletContext sc, int orderId) {
        Order order = null;
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



        return order;
    }

    public void updateOrder(ServletContext sc, int id, Order newOrder) {
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



    }

    public void deleteOrder(ServletContext sc, Order order) {
        int id = order.getId().intValue();

        String sql = "DELETE FROM `smart_portables`.`order` WHERE `seq_no`= ?;";

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

	/**
	 * Get number of product in the given category 
	 */
	public int getProductCountCategory(String category) {
		int count = -1;
		String sql = "SELECT count(*) from smart_portables.product WHERE category = ?";
		
		try(PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, category);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			count = rs.getInt(1);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public void insertProduct(Product product) {
		List<Product> listProduct = new ArrayList<>();
		listProduct.add(product);
		initListProduct(listProduct);
	}
	
    public int selectProductSeqNo(String category, int productId) {
		String sql = "SELECT seq_no from smart_portables.product WHERE category = ? AND product_id = ?";
		int seqNo = -1;
		
		try(PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, category);
			ps.setInt(2, productId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			seqNo = rs.getInt("seq_no");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return seqNo;
	}

	/**
	 * Select all product with given category.
	 * Return a map of <product ID, product>
	 */
	public Map<Integer, Product> selectProduct(String category) {
		Map<Integer, Product> mapProduct = new LinkedHashMap<>();
		String sql = "SELECT * from smart_portables.product WHERE category = ?"
					+ " ORDER BY product_id asc"
		;
		
		try(PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, category);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Product p = buildProductObject(rs);
				mapProduct.put(p.getId(), p);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return mapProduct;
	}

	public Product selectProduct(int seqNo) {
		String sql = "SELECT * from smart_portables.product WHERE seq_no = ?";
		Product p = null;
		
		try(PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, seqNo);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				p = buildProductObject(rs);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return p;
	}

	/**
	 * Update product with new info, given seq_no
	 */
	public void updateProduct(int seqNo, Product updatedProduct) {
		String sql	= "UPDATE `smart_portables`.`product` SET"
					+ " `discount`= ?, "
					+ " `rebate`= ?, "
					+ " `name`= ?, "
					+ " `price`= ?, "
					+ " `image`= ?, "
					+ " `amount`= ?"
					+ " WHERE `seq_no`= ?";

		
		try(PreparedStatement ps = conn.prepareStatement(sql)) {
			Double discount = updatedProduct.getDiscount();
			Double rebate = updatedProduct.getRebate();
			if(discount == null) {
				ps.setNull(1, Types.DECIMAL);
			}
			else {
				ps.setDouble(1, discount);
			}
			if(rebate == null) {
				ps.setNull(2, Types.DECIMAL);
			}
			else {
				ps.setDouble(2, rebate);
			}			
			ps.setString(3, updatedProduct.getName());
			ps.setDouble(4, updatedProduct.getPrice());
			ps.setString(5, updatedProduct.getImage());
			ps.setInt(6, updatedProduct.getAmount());
			ps.setInt(7, seqNo);
			
			ps.execute();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 *	Delete product and all product-accessories relations from DB
	 */
	public void deleteProduct(int seqNo) {
		String sqlDeleteProduct = "DELETE FROM `smart_portables`.`product` WHERE `seq_no`= ?;";
		String sqlDeleteRelation = "DELETE FROM `smart_portables`.`product_accessories` WHERE product = ?";
		
		try(PreparedStatement ps = conn.prepareStatement(sqlDeleteProduct)) {
			ps.setInt(1, seqNo);
			ps.execute();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		try(PreparedStatement ps = conn.prepareStatement(sqlDeleteRelation)) {
			ps.setInt(1, seqNo);
			ps.execute();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

	/**
     * Init list product into table 'product' on context initialization
     * Also, insert all accessories-product relation into table 'product_accessories'
     */
    public void initListProduct(List<Product> listProduct) {
        String sqlInsertProduct = "INSERT INTO `smart_portables`.`product` "
                            + "(`category`, `product_id`, `name`, `image`, `price`, `discount`, `rebate`, `amount`) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);"
        ;
        String sqlInsertRelation = "INSERT INTO `smart_portables`.`product_accessories` (`product`, `accessories`)"
        							+ " VALUES (?, ?);";

        for(Product p: listProduct) {
            try(PreparedStatement ps = conn.prepareStatement(sqlInsertProduct)) {
                ps.setString(1, p.getCategory());
                ps.setInt(2, p.getId());
                ps.setString(3, p.getName());
                ps.setString(4, p.getImage());
                ps.setDouble(5, p.getPrice());
                if(p.getDiscount() == null) {
                    ps.setNull(6, Types.DECIMAL);
                }
                else {
                    ps.setDouble(6, p.getDiscount());
                }
                if(p.getRebate() == null) {
                    ps.setNull(7, Types.DECIMAL);
                }
                else {
                    ps.setDouble(7, p.getRebate());
                }
                ps.setInt(8, p.getAmount());
                ps.execute();
            }
            catch(SQLException e) {
                e.printStackTrace();
            }
            if(p.getListAccessoryId() != null) {
                for(Integer i: p.getListAccessoryId()) {
                    try(PreparedStatement ps = conn.prepareStatement(sqlInsertRelation)) {
                        ps.setInt(1, p.getId());
                        ps.setInt(2, i);
                        ps.execute();
                    }
                    catch(SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    private Product buildProductObject(ResultSet rs) throws SQLException {
		Product p = new Product();
		p.setCategory(rs.getString("category"));
		p.setId(rs.getInt("product_id"));
		p.setName(rs.getString("name"));
		p.setImage(rs.getString("image"));
		p.setPrice(rs.getDouble("price"));
		p.setDiscount(rs.getDouble("discount"));
		p.setRebate(rs.getDouble("rebate"));
		p.setAmount(rs.getInt("amount"));
		p.setListAccessoryId(p.getCategory().equals("accessory") ? null :
			selectProductAccessories(rs.getInt("seq_no")));
		return p;
	}

	
	/**
	 * Return list of accessories associated with given product seq_no 
	 */
	private List<Integer> selectProductAccessories(int seqNo) {
		List<Integer> listAccessories = null;
		String sql = "SELECT * from product_accessories WHERE product = ?";
		
		try(PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, seqNo);
			ResultSet rs = ps.executeQuery();
			if(!rs.first()) {
				listAccessories = new ArrayList<>();
				
				while(rs.next()) {
					Integer accessoryId = Integer.valueOf(rs.getInt("accessories"));
					listAccessories.add(accessoryId);
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return listAccessories;
	}

	/**
     * Initialize list category on context initialization
     */
    public void initListCategory(List<Category> listCategory) {
        String sql =      "INSERT INTO `smart_portables`.`category` (`id`, `name`, `image-overview`) "
                        + "VALUES (?, ?, ?);" 
        ;
        for(Category cat: listCategory) {
            try(PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, cat.getId());
                ps.setString(2, cat.getName());
                ps.setString(3, cat.getImageName());
                ps.execute();
            }
            catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Category> selectAllCategory() {
        String sql = "SELECT * from smart_portables.category";
        List<Category> listCategory = new ArrayList<>();

        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Category cat = new Category(
                      rs.getString(1)   // id
                    , rs.getString(2)   // name
                    , rs.getString(3)   // imageName
                );
                listCategory.add(cat);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return listCategory;
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

    public void initConnection(ServletContext sc) {
        String propertiesFullFilePath = sc.getRealPath(PROPERTIES_FILE_PATH);
        try(InputStream input = new FileInputStream(propertiesFullFilePath)) {
            Properties prop = new Properties();
            prop.load(input);
            String sqlUrl = "jdbc:mysql://"
                +   prop.getProperty("database_url")
                +   ":"
                +   prop.getProperty("port")
                +   "/" + prop.getProperty("schema")
                +   "?useSSL=false"
            ;
            String dbUser = prop.getProperty("dbuser");
            String dbPassword = prop.getProperty("password");
            conn = DriverManager.getConnection(sqlUrl, dbUser, dbPassword);

            System.out.println("Connected to MySQL server " + sqlUrl);
        }
        catch(SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if(conn != null) {
            try {
                conn.close();
            }
            catch(SQLException e) {
                e.printStackTrace();
            }
        }
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

	public void truncateTable(String tableName) {
	    String sql = "TRUNCATE TABLE " + tableName + ";";
	    try(PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.execute();
	    }
	    catch(SQLException e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * Reset auto increment to 1 on given table
	 */
    public void resetAutoIncrement(String tableName) {
    	String sql = "ALTER TABLE " + tableName + " AUTO_INCREMENT = 1";
    	
    	try(PreparedStatement ps = conn.prepareStatement(sql)) {
    		ps.execute();
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
    	}
	}
}

