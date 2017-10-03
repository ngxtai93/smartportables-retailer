package tai.entity;

public class User {
    private String username;
    private String password;
    private Role role;
    private ShoppingCart cart;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Role getRole() {
        return this.role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public ShoppingCart getShoppingCart() {
        return this.cart;
    }
    public void setShoppingCart(ShoppingCart cart) {
        this.cart = cart;
    }

}