<%@ page import = "tai.Product" %>
<%@ page import = "java.text.NumberFormat, java.util.*" %>

<%
    String rootPath = request.getContextPath();
    String currentCategory = (String) request.getAttribute("current-category");
    Product product = (Product) request.getAttribute("current-product");
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
    Map<Integer, Product> mapProductAccessories = (Map<Integer, Product>) request.getAttribute("product-accessory");
%>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<div class="complete-your-purchase">
    <div class="heading-wrapper">
        <h2 class="title">
            Complete Your Purchase
        </h2>
    </div>
    <div id="accessory-carousel" class="carousel slide" data-ride="carousel">
        <div class="carousel-inner" role="listbox">
            <%for(Map.Entry<Integer,Product> entry: mapProductAccessories.entrySet()) {
                Product accessory = entry.getValue();
            %>
                <div class="item<%=entry.getKey() == 1 ? " active" : ""%>">
                    <img id="bootstrap-override" class="carousel-image"
                     src="<%=rootPath%>/resources/images/product/accessory/<%=accessory.getImage()%>">
                    <div class="container">
                        <div class="carousel-caption">
                            <span class="accessory-caption"><%=accessory.getName()%></span>
                            <br>
                            <span class="price"><%=currencyFormatter.format(accessory.getPrice() - accessory.getDiscount())%></span>
                            <br>
                            <%if(accessory.getRebate() > 0) { %>
                                <span class="price">Get <%=currencyFormatter.format(accessory.getRebate())%> in rebate!</span>
                            <% } %>
                            <div class="add-to-cart-button">
                                <form method="post" action="<%=request.getContextPath()%>/cart/add">
                                    <input type="hidden" name="category" value="accessory">
                                    <input type="hidden" name="product-id" value="<%=product.getId()%>">
                                    <button class="button-cart" type="submit">
                                        Add to cart
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            <% }%>
            <a class="left carousel-control" href="#accessory-carousel" role="button" data-slide="prev">
                <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                <span class="sr-only">Previous</span>
            </a>
            <a class="right carousel-control" href="#accessory-carousel" role="button" data-slide="next">
                <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                <span class="sr-only">Next</span>
            </a>
        </div>
    </div>
</div>