<%@include file = "../partials/header.jsp" %>
<%@ page import = "java.util.*, java.text.NumberFormat" %>
<%@ page import = "tai.entity.Product" %>

<%
    List<Product> listAllProduct = (List<Product>) request.getAttribute("listAllProduct");
    int productCount = 1;
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
%>

<div id="body">
    <section class="content">

        <h4><a href="<%=rootPath%>/account/report/inventory/"><b>Back to menu</b></a></h4>
        <table>
            <tr>
                <th>No.</th>
                <th>Product</th>
                <th>Price</th>
                <th>Available</th>
            </tr>
            <% for(Product p: listAllProduct) { %>
                <tr>
                    <th><%=productCount%></th>
                    <th><%=p.getName()%></th>
                    <th><%=currencyFormatter.format(p.getPrice())%></th>
                    <th><%=p.getAmount()%></th>
                </tr>
                <% productCount++; %>
            <% } %>
        </table>
    </section>
<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>