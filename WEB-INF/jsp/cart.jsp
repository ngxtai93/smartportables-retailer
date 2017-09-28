<%@ page import = "java.util.*" %>
<%@ page import = "java.text.NumberFormat" %>
<%@ page import = "tai.ShoppingCart, tai.User, tai.Product" %>

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
    <% if(count == 0) { %>
        <h1><b>Nothing in cart.</b></h1>
    <% }
    else { %>
        <div class="cart-header">
            <br>
            <h2 id="cart-heading-text">Your Cart <i><%=count%> Item</i></h2>
            <div class="add-to-cart-button">
            <button class="button-cart">
                Checkout
            </button>
            </div>
        </div>
        <% for(Map.Entry<Product, Integer> entry: cart.getListProduct().entrySet()) {
            Product product = entry.getKey();
            Integer amount = entry.getValue(); %> 
            <div class="cart-product-description">
                <div class="col-1">
                    <img class="product-image"
                     src="<%=rootPath%>/resources/images/product/<%=product.getCategory()%>/<%=product.getImage()%>"
                     height="100" width="100">
                </div>
                <div class="col-2">
                    <span><a href="<%=rootPath%>/product/<%=product.getCategory()%>/<%=product.getId()%>"><%=product.getName()%></a></span>
                </div>
                <div class="col-3">
                    Quantity: <input type="text" size="3" value="<%=amount%>">
                </div>
                <div class="col-4">
                    <span class="price"><%=currencyFormatter.format(product.getPrice() - product.getDiscount() * amount)%></span>
                    <% if(product.getDiscount() > 0) { %> 
                        <div class="sale-message">
                        On Sale
                        </div>
                        <div class="savings">
                        Save <%=currencyFormatter.format(product.getDiscount() * amount)%>
                        </div>
                        <div class="regular-price">
                        Was <%=currencyFormatter.format(product.getPrice() * amount)%>
                        </div>
                    <% } %>
                </div>
            </div>
            <br>
        <% } %>
    <% } %>
    </section>

<%@include file = "./partials/sidebar.jsp" %>
</div>
<%@include file = "./partials/footer.jsp" %>