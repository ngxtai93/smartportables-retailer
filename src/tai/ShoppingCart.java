package tai;

import java.util.ArrayList;

public class ShoppingCart {
    private ArrayList<Product> listItem;
    private String cartId;

    public ShoppingCart() {
        this.listItem = new ArrayList<>();
    }

    public ArrayList<Product> getListItem() {
        return this.listItem;
    }

    public void setListItem(ArrayList<Product> listItem) {
        this.listItem = listItem;
    }

    public String getCartId() {
        return this.cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }


    public int countItem() {
        return listItem.size();
    }
}