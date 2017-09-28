<%@ page import = "java.util.*" %>
<%@ page import = "java.text.NumberFormat" %>
<%@ page import = "tai.ProductManager, tai.ShoppingCart, tai.User, tai.Product" %>

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
        <form>
            <table>
                <tr>
                    <th><label for="name">Full name:</label></th>
                    <td><input required id="name" type="text" size="50" maxlength="50"><br></td>
                </tr>
                <tr>
                    <th><label for="address">Address:</label></th>
                    <td><input required id="address" type="text" size="50" maxlength="50"><br></td>
                </tr>
                <tr>
                    <th><label for="city">City: </label></th>
                    <td><input required id="city" type="text" size="50" maxlength="50"><br></td>
                </tr>
                <tr>
                    <th><label for="state">State: </label></th>
                    <td><input required id="state" type="text" size="50" maxlength="50"><br></td>
                </tr>
                <tr>
                    <th><label for="zip">ZIP</label></th>
                    <td><input required id="zip" type="text" size="50" maxlength="50"><br></td>
                </tr>
                <tr>
                    <th><label for="phone">Phone number:</label></th>
                    <td><input required id="phone" type="text" size="50" maxlength="50"><br></td>
                </tr>
            </table>
            <br><br>
            <table>
                <tr>
                    <th><label for="cc-num">Credit card number:</label></th>
                    <td><input required id="cc-num" type="text" size="50" maxlength="50"><br></td>
                </tr>
                <tr>
                    <th><label for="cc-exp">Expiration date(MM/YY):</label></th>
                    <td><input required id="cc-exp" type="text" size="5" maxlength="5"><br></td>
                </tr>
            </table>

            <div class="add-to-cart-button">
                <form action="<%=rootPath%>/checkout" method="post">
                    <button class="button-cart">
                        Place order
                    </button>
                </form>
            </div>
        </form>
        <br>
    <% } %>
    </section>  
<%@include file = "./partials/sidebar.jsp" %>
</div>
<%@include file = "./partials/footer.jsp" %>