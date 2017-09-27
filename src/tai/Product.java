package tai;

import java.util.ArrayList;

public class Product {
    private Integer id;
    private String category;
    private String name;
    private String image;
    private Double price;
    private Double discount;
    private ArrayList<Integer> listAccessoryId;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("category: " + (category == null ? "null" : category + "\n"))
            .append("id: " + (id == null ? "null" : String.valueOf(id + "\n")))
            .append("name: " + (name == null ? "null" : name + "\n"))
            .append("image: " + (image == null ? "null" : image + "\n"))
            .append("price: " + (price == null ? "null" : String.valueOf(price + "\n")))
            .append("discount: " + (discount == null ? "null" : String.valueOf(discount + "\n")))
        ;
        return result.toString();
    }
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public ArrayList<Integer> getListAccessoryId() {
        return this.listAccessoryId;
    }

    public void setListAccessoryId(ArrayList<Integer> listAccessoryId) {
        this.listAccessoryId = listAccessoryId;
    }
}