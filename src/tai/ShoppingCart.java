package tai;

import java.util.ArrayList;

public class ShoppingCart {
    private ArrayList<Product> listItem;

    public ShoppingCart() {
        this.listItem = new ArrayList<>();
    }

    public ArrayList<Product> getListItem() {
        return this.listItem;
    }

    public void setListItem(ArrayList<Product> listItem) {
        this.listItem = listItem;
    }

    public int countItem() {
        return listItem.size();
    }
}