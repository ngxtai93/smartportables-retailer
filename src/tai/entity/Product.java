package tai.entity;

import java.util.ArrayList;

public class Product {
    private Integer id;
    private String category;
    private String name;
    private String image;
    private Double price;
    private Double discount;
    private Double rebate;
    private Integer amount;
    private ArrayList<Integer> listAccessoryId;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("category: " + (category == null ? "null\n" : category + "\n"))
            .append("id: " + (id == null ? "null\n" : String.valueOf(id + "\n")))
            .append("name: " + (name == null ? "null\n" : name + "\n"))
            .append("image: " + (image == null ? "null\n" : image + "\n"))
            .append("price: " + (price == null ? "null\n" : String.valueOf(price + "\n")))
            .append("discount: " + (discount == null ? "null\n" : String.valueOf(discount + "\n")))
            .append("rebate: " + (rebate == null ? "null\n" : String.valueOf(rebate + "\n")))
            .append("amount: " + (amount == null ? "null\n" : String.valueOf(amount + "\n")))
        ;
        StringBuilder listAccessoryIdStr = new StringBuilder();
        listAccessoryIdStr.append("accessory-id: ");
        if(listAccessoryId == null) {
            listAccessoryIdStr.append("null");
        }
        else {
            for(int i = 0; i < listAccessoryId.size(); i++) {
                listAccessoryIdStr.append(listAccessoryId.get(i));
                if(i != listAccessoryId.size() - 1) {
                    listAccessoryIdStr.append(", ");
                }
            }
        }
        result.append(listAccessoryIdStr.toString());
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Product product = (Product) obj;
        return (
            (this.id == product.id || (product.id != null && this.id.equals(product.id)))
        &&  (this.category == product.category || (product.category != null && this.category.equals(product.category)))
        &&  (this.name == product.name || (product.name != null && this.name.equals(product.name)))
        &&  (this.image == product.image || (product.image != null && this.image.equals(product.image)))
        &&  (this.price == product.price || (product.price != null && this.price.equals(product.price)))
        &&  (this.discount == product.discount || (product.discount != null && this.discount.equals(product.discount)))
        &&  (this.listAccessoryId == product.listAccessoryId || (product.listAccessoryId != null && this.listAccessoryId.equals(product.listAccessoryId)))
        );
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash;
        hash = 31 * hash
         + (null == listAccessoryId ? 0 : listAccessoryId.hashCode())
         + (null == id ? 0 : id.hashCode())
         + (null == category ? 0 : category.hashCode())
         + (null == name ? 0 : name.hashCode())
         + (null == image ? 0 : image.hashCode())
         + (null == price ? 0 : price.hashCode())
         + (null == discount ? 0 : discount.hashCode())
         + (null == rebate ? 0 : rebate.hashCode())
        ;
        return hash;
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

    public Double getRebate() {
        return this.rebate;
    }

    public void setRebate(Double rebate) {
        this.rebate = rebate;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public ArrayList<Integer> getListAccessoryId() {
        return this.listAccessoryId;
    }

    public void setListAccessoryId(ArrayList<Integer> listAccessoryId) {
        this.listAccessoryId = listAccessoryId;
    }
}