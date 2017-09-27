package tai;


import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingCart {
    private LinkedHashMap<Product, Integer> listProduct;
    private String cartId;

    public ShoppingCart() {
        this.listProduct = new LinkedHashMap<>();
    }

    public LinkedHashMap<Product, Integer> getListProduct() {
        return this.listProduct;
    }

    public void setListProduct(LinkedHashMap<Product, Integer> listProduct) {
        this.listProduct = listProduct;
    }

    public String getCartId() {
        return this.cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }


    public int countItem() {
        int count = 0;
        for(Map.Entry<Product, Integer> entry: this.listProduct.entrySet()) {
            count += entry.getValue();
        }

        return count;
    }
}