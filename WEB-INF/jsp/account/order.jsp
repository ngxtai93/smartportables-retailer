<%@include file = "../partials/header.jsp" %>
<%@ page import = "tai.entity.Order, tai.entity.Product" %>
<%@ page import = "java.util.*, java.time.format.DateTimeFormatter, java.time.LocalDate, java.text.NumberFormat" %>
<%
    List<Order> listOrder = (List<Order>) request.getAttribute("list-order");
    int orderCount = 0;
    if(listOrder != null) {
        orderCount = listOrder.size();
    }
    DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("MMMM dd, uuuu");
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
 %>

<div id="body">
    <section class="content">
        <br>
        <% if(orderCount == 0) { %>
            <p>You have no order available.</p>
        <% }
        else { %>
            <p>You have <%=listOrder.size()%> orders placed.</p>
            <br>
            <% for(Order order: listOrder) { %>
                <div class="order">
                    <div class="order-header">
                        <div class="col-1">
                            <p>Order placed</p>
                            <p><%=fullFormatter.format(order.getOrderDate())%></p>
                        </div>
                        <div class="col-2">
                            <p>Total</p>
                            <p><%=currencyFormatter.format(order.getTotalPrice())%></p>
                        </div>
                        <div class="col-3">
                            <p>Ship to</p>
                            <p><%=order.getName()%></p>
                        </div>
                        <div class="col-4">
                            <p>Order #<%=order.getId()%></p>
                            <p><b>Order <%=order.getStatus() %></b></p>
                        </div>
                        <div class="col-5">
                            <p>Confirmation number: <%=order.getConfirmNumber()%></p>
                            <form method="post" action="<%=rootPath%>/account/order/cancel">
                                <input type="hidden" name="order-id" value="<%=order.getId()%>"> 
                                <button type="submit">Cancel Order</button>
                            </form>
                        </div>
                    </div>
                    <% for(Map.Entry<Product, Integer> entry: order.getListProduct().entrySet()) {
                        Product product = entry.getKey();
                        Integer amount = entry.getValue(); %>

                        <div class="order-product">
                            <% if(!order.getStatus().equals("Cancelled")) { %>
                                <p><b>Arriving <%=fullFormatter.format(order.getDeliverDate())%></b></p>
                            <% } %>

                            <div class="col-1">
                                <img class="product-image"
                                 src="<%=rootPath%>/resources/images/product/<%=product.getCategory()%>/<%=product.getImage()%>">
                            </div>
                            <div class="col-2">
                                <%=amount > 1 ? amount + "x " : ""%>
                                <a href="<%=rootPath%>/product/<%=product.getCategory()%>/<%=product.getId()%>">
                                    <%=product.getName()%>
                                </a>
                                <br>
                                <p><%=currencyFormatter.format((product.getPrice() - product.getDiscount()) * amount)%></p>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } %>
        <% } %>
    </section>

<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>