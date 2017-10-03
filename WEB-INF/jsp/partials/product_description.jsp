<%@ page import = "java.util.*" %>
<%@ page import = "java.text.NumberFormat" %>
<%@ page import = "tai.entity.Product" %>

<% 
    String rootPath = request.getContextPath();
    String currentCategory = (String) request.getAttribute("current-category");
    Map<Integer, Product> mapProduct = (Map<Integer, Product>) request.getAttribute("mapProduct");
    Product product =   mapProduct.get(
        (Integer) request.getAttribute("current-product-id")
    );
    // format
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

%>

<div class="product-description">
  <div class="col-1">
    <img class="product-image" src="<%=request.getContextPath()%>/resources/images/product/<%=currentCategory%>/<%=product.getImage()%>">
  </div>
  <div class="col-2">
    <span><a href="<%=rootPath%>/product/<%=currentCategory%>/<%=product.getId()%>"><%=product.getName()%></a></span>
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
    <% if(product.getRebate() != null && product.getRebate() > 0) { %> 
        <div class="savings">
        Save <%=currencyFormatter.format(product.getRebate())%> in rebate
        </div>
    <% } %>
    <div class="add-to-cart-button">
      <form method="post" action="<%=request.getContextPath()%>/cart/add">
          <input type="hidden" name="category" value="<%=currentCategory%>">
          <input type="hidden" name="product-id" value="<%=product.getId()%>">
          <button class="button-cart" type="submit">
              Add to cart
          </button>
      </form>
    </div>
  </div>
</div>