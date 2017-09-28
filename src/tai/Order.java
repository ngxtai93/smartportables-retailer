package tai;

import java.time.LocalDate;
import java.util.*;

public class Order {
    private Integer id;
    private String username;

    private ArrayList<Product> orderProduct;

    private String name;
    private String address;
    private String city;
    private String state;
    private Integer zip;
    private Integer phone;

    private Integer creditCardNum;
    private LocalDate expireDate;

    private String status;

    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Product> getListProduct() {
        return this.orderProduct;
    }

    public void setListProduct(ArrayList<Product> orderProduct) {
        this.orderProduct = orderProduct;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        this.state = state;
    }

    
    public Integer getZip() {
        return this.zip;
    }
    public void setZip(Integer zip) {
        this.zip = zip;
    }
    
    public Integer getPhone() {
        return this.phone;
    }
    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Integer getCreditCardNum() {
        return this.creditCardNum;
    }
    public void setCreditCardNum(Integer creditCardNum) {
        this.creditCardNum = creditCardNum;
    }

    public LocalDate getExpireDate() {
        return this.expireDate;
    }
    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}