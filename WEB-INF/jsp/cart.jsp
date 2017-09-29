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
    ProductManager pm = new ProductManager();
    Double totalSaving = Double.valueOf(0);
    Double totalPrice = Double.valueOf(0);

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
                <form action="<%=rootPath%>/checkout" method="get">
                    <button class="button-cart">
                        Checkout
                    </button>
                </form>
            </div>
        </div>
        <% for(Map.Entry<Product, Integer> entry: cart.getListProduct().entrySet()) {
            Product product = entry.getKey();
            //System.out.println(product);
            Integer amount = entry.getValue();
            Double netPrice = (product.getPrice() - product.getDiscount()) * amount;
            totalPrice += netPrice;
            totalSaving += product.getDiscount();  %>
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
                    <form method="post" action="<%=rootPath%>/cart/update" id="update">
                        Quantity: <input type="text" size="3" name="amount" value="<%=amount%>">
                        <input type="hidden" name="cart-id" value="<%=cart.getCartId()%>">
                        <input type="hidden" name="product-category" value="<%=product.getCategory()%>">
                        <input type="hidden" name="product-id" value="<%=product.getId()%>">
                        <button type="submit">Update</button>
                    </form>
                    <form method="post" action="<%=rootPath%>/cart/delete" id="delete">
                        <input type="hidden" name="cart-id" value="<%=cart.getCartId()%>">
                        <input type="hidden" name="product-category" value="<%=product.getCategory()%>">
                        <input type="hidden" name="product-id" value="<%=product.getId()%>">
                        <button type="submit">Remove</button>
                    </form>
                    
                </div>
                <div class="col-4">
                    <span class="price"><%=currencyFormatter.format(netPrice)%></span>
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
            <% Map<Integer, Product> mapProductAccessories = pm.getProductAccessories(request, product); %>
            <% if(mapProductAccessories != null && mapProductAccessories.size() > 0) {
                    request.setAttribute("product-accessory", mapProductAccessories);
                    request.setAttribute("current-product", product); %>
                    <jsp:include page="/WEB-INF/jsp/partials/accessories_carousel.jsp"/>
            <% } %>
        <% } %>
        <div class="cart-summary">
            <% if(totalSaving > 0) { %>
                <p>Total saved: <%=currencyFormatter.format(totalSaving)%></p>
            <% } %>
            <p>Product total: <b><%=currencyFormatter.format(totalPrice)%></b></p>
            <div class="add-to-cart-button">
                <form action="<%=rootPath%>/checkout" method="get">
                    <button class="button-cart">
                        Checkout
                    </button>
                </form>
            </div>
        </div>
    <% } %>
    </section>

<%@include file = "./partials/sidebar.jsp" %>
</div>
<%@include file = "./partials/footer.jsp" %>