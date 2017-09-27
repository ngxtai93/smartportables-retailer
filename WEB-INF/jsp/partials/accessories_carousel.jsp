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
                            <div class="add-to-cart-button">
                                <button class="button-cart">
                                    Add to cart
                                </button>
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