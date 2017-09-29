package tai;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

public class Order {
    private Integer id;
    private String username;
    private LocalDate orderDate;
    private LocalDate deliverDate;
    private Long confirmNumber;

    private LinkedHashMap<Product, Integer> orderProduct;

    private String name;
    private String address;
    private String city;
    private String state;
    private Integer zip;
    private Integer phone;

    private BigInteger creditCardNum;
    private LocalDate expireDate;

    private String status;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb  .append("id: ")
            .append(id == null ? "null" : this.id)
            .append("\n")
            .append("username: ")
            .append(username == null ? "null" : this.username)
            .append("\n")
            .append("name: ")
            .append(name == null ? "null" : this.name)
            .append("\n")
            .append("address: ")
            .append(address == null ? "null" : this.address)
            .append("\n")
            .append("city: ")
            .append(city == null ? "null" : this.city)
            .append("\n")
            .append("state: ")
            .append(state == null ? "null" : this.state)
            .append("\n")
            .append("zip: ")
            .append(zip == null ? "null" : this.zip)
            .append("\n")
            .append("phone: ")
            .append(phone == null ? "null" : this.phone)
            .append("\n")
            .append("\n")
            .append("phone: ")
            .append(phone == null ? "null" : this.phone)
            .append("\n")
            .append("creditCardNum: ")
            .append(creditCardNum == null ? "null" : this.creditCardNum)
            .append("\n")
            .append("expiration: ")
            .append(expireDate == null ? "null" : 
                (this.expireDate.getMonthValue() + "/" + this.expireDate.getYear()))
            .append("\n")
        ;
        return sb.toString();
    }

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

    public LocalDate getOrderDate() {
        return this.orderDate;
    }
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getDeliverDate() {
        return this.deliverDate;
    }
    public void setDeliverDate(LocalDate deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Long getConfirmNumber() {
        return this.confirmNumber;
    }
    public void setConfirmNumber(Long confirmNumber) {
        this.confirmNumber = confirmNumber;
    }

    public LinkedHashMap<Product, Integer> getListProduct() {
        return this.orderProduct;
    }

    public void setListProduct(LinkedHashMap<Product, Integer> orderProduct) {
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

    public BigInteger getCreditCardNum() {
        return this.creditCardNum;
    }
    public void setCreditCardNum(BigInteger creditCardNum) {
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