package tai.entity;

import java.time.LocalDate;

public class Review {
    private String productName;
    private String productCategory;
    private Double productPrice;
    private String retailerName;
    private Integer retailerZip;
    private String retailerCity;
    private String retailerState;
    private Boolean productOnSale;
    private String manufacturerName;
    private Boolean manufacturerRebate;
    private String username;
    private Integer userAge;
    private String gender;
    private String occupation;
    private Double rating;
    private LocalDate reviewDate;
    private String reviewText;

    public String getProductName() {
        return this.productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductCategory() {
        return this.productCategory;
    }
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
    public Double getProductPrice() {
        return this.productPrice;
    }
    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
    public String getRetailerName() {
        return this.retailerName;
    }
    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }
    public Integer getRetailerZip() {
        return this.retailerZip;
    }
    public void setRetailerZip(Integer retailerZip) {
        this.retailerZip = retailerZip;
    }
    public String getRetailerCity() {
        return this.retailerCity;
    }
    public void setRetailerCity(String retailerCity) {
        this.retailerCity = retailerCity;
    }
    public String getRetailerState() {
        return this.retailerState;
    }
    public void setRetailerState(String retailerState) {
        this.retailerState = retailerState;
    }
    public Boolean getProductOnSale() {
        return this.productOnSale;
    }
    public void setProductOnSale(Boolean productOnSale) {
        this.productOnSale = productOnSale;
    }
    public String getManufacturerName() {
        return this.manufacturerName;
    }
    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }
    public Boolean getManufacturerRebate() {
        return this.manufacturerRebate;
    }
    public void setManufacturerRebate(Boolean manufacturerRebate) {
        this.manufacturerRebate = manufacturerRebate;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getUserAge() {
        return this.userAge;
    }
    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getOccupation() {
        return this.occupation;
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    public Double getRating() {
        return this.rating;
    }
    public void setRating(Double rating) {
        this.rating = rating;
    }
    public LocalDate getReviewDate() {
        return this.reviewDate;
    }
    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }
    public String getReviewText() {
        return this.reviewText;
    }
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}