<%@ page import = "java.util.*" %>
<%@ page import = "java.text.NumberFormat" %>
<%@ page import = "tai.model.ProductManager, tai.entity.ShoppingCart, tai.entity.User, tai.entity.Product" %>

<%@include file = "./partials/header.jsp" %>
<%
    ShoppingCart cart = currentUser.getShoppingCart();
    int count = 0;
    if(cart != null) {
        count = cart.countItem();
    }
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
%>

<div id="body">
    <section class="content">
    <br>
    <% if(count == 0) { %>
        <p>No product to checkout.</p>
        <p><a href="<%=rootPath%>">Back to homepage</a></p>
    <% }
    else { %>
        <form method="post">
            <table>
                <tr>
                    <th><label for="name">Full name:</label></th>
                    <td><input required name="name" type="text" size="50" maxlength="50"><br></td>
                </tr>
                <tr>
                    <th><label for="address">Address:</label></th>
                    <td><input required name="address" type="text" size="50" maxlength="50"><br></td>
                </tr>
                <tr>
                    <th><label for="city">City: </label></th>
                    <td><input required name="city" type="text" size="50" maxlength="50"><br></td>
                </tr>
                <tr>
                    <th><label for="state">State: </label></th>
                    <td><input required name="state" type="text" size="50" maxlength="50"><br></td>
                </tr>
                <tr>
                    <th><label for="zip">ZIP</label></th>
                    <td><input required name="zip" type="text" size="50" maxlength="50"><br></td>
                </tr>
                <tr>
                    <th><label for="phone">Phone number:</label></th>
                    <td><input required name="phone" type="text" size="50" maxlength="50"><br></td>
                </tr>
            </table>
            <br><br>
            <table>
                <tr>
                    <th><label for="cc-num">Credit card number:</label></th>
                    <td><input required name="cc-num" type="text" size="50" maxlength="50"><br></td>
                </tr>
                <tr>
                    <th><label for="cc-exp">Expiration date(MM/YY):</label></th>
                    <td><input required name="cc-exp" type="text" size="5" maxlength="5"><br></td>
                </tr>
            </table>

            <div class="add-to-cart-button">
                <button class="button-cart" type="submit">
                    Place order
                </button>
            </div>
        </form>
        <br>
    <% } %>
    </section>  
<%@include file = "./partials/sidebar.jsp" %>
</div>
<%@include file = "./partials/footer.jsp" %>