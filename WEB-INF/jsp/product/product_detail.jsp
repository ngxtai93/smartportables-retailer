<%@ page import = "tai.Product" %>
<%@ page import = "java.text.NumberFormat, java.util.*" %>
<%@include file = "../partials/header.jsp" %>

<%
    Product product = (Product) request.getAttribute("current-product");
    String currentCategory = (String) request.getAttribute("current-category");
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
    Map<Integer, Product> mapProductAccessories = (Map<Integer, Product>) request.getAttribute("product-accessory");
%>

<div id="body">
    <section class="content">
        <div class="product-detail">
            <div class="row">
                <div class="col-1">
                    <span class="product-header"><%=product.getName()%></span>
                    <div class="product-image-wrapper">
                        <img class="product-image" 
                        src="<%=request.getContextPath()%>/resources/images/product/<%=currentCategory%>/<%=product.getImage()%>">
                    </div>
                </div>
                <div class="col-2">
                    <div class="add-to-cart-button">
                        <form method="post" action="<%=request.getContextPath()%>/cart/add">
                            <input type="hidden" name="category" value="<%=currentCategory%>">
                            <input type="hidden" name="product-id" value="<%=product.getId()%>">
                            <button class="button-cart" type="submit">
                                Add to cart
                            </button>
                        </form>
                    </div>
                    <span class="price"><%=currencyFormatter.format(product.getPrice() - product.getDiscount())%></span>
                    <% if(product.getDiscount() > 0) { %> 
                        <div class="sale-message">
                            On Sale
                        </div>
                        <div class="savings">
                        Save <%=currencyFormatter.format(product.getDiscount())%>
                        </div>
                        <div class="regular-price">
                        Was <%=currencyFormatter.format(product.getPrice())%>
                        </div>
                    <% } %>
                </div>
            </div>
            <% if(mapProductAccessories != null) { %>
                <jsp:include page="/WEB-INF/jsp/partials/accessories_carousel.jsp"/>
            <% } %>
        </div>
    </section>

<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>