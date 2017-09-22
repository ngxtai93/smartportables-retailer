<%@ page import = "java.util.*" %>
<%@ page import = "java.text.NumberFormat" %>
<%@ page import = "tai.Product" %>

<% 
    String currentCategory = (String) request.getAttribute("current-category");
    Map<Integer, Product> mapProduct = (Map<Integer, Product>) request.getAttribute("mapProduct");
    Product product =   mapProduct.get(
        (Integer) request.getAttribute("current-product-id")
    );
    // format
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

%>

<div class="product-detail">
  <div class="col-1">
    <img src="<%=request.getContextPath()%>/resources/images/product/<%=currentCategory%>/<%=product.getImage()%>">
  </div>
  <div class="col-2">
    <span><%=product.getName()%></span>
  </div>
  <div class="col-3">
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
    <div class="add-to-cart-button">
      <button class="button-cart">
        Add to cart
      </button>
    </div>
  </div>
</div>