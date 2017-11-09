<%@include file = "../partials/header.jsp" %>
<%@ page import = "java.util.*, java.text.NumberFormat" %>
<%@ page import = "tai.entity.Product" %>
<%@ page import = "tai.utils.StringUtilities" %>

<%
    Map<Product, Integer> mapProductAmount = (Map<Product, Integer>) request.getAttribute("mapProductAmount");
    int productCount = 1;
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
    StringUtilities stringUtil = StringUtilities.INSTANCE;
%>

<div id="body">
    <section class="content">

        <h4><a href="<%=rootPath%>/account/report/sales/"><b>Back to menu</b></a></h4>
        <table>
            <tr>
                <th>No.</th>
                <th>Product</th>
                <th>Amount</th>
                <th>Price</th>
                <th>Total</th>
            </tr>
            <% for(Map.Entry<Product, Integer> entry: mapProductAmount.entrySet()) {
                Product p = entry.getKey();
                Integer amount = entry.getValue(); %>
                <tr>
                    <th><%=productCount%></th>
                    <th><%=stringUtil.filter(p.getName())%></th>
                    <th><%=amount%></th>
                    <th><%=currencyFormatter.format(p.getPrice())%></th>
                    <th><%=currencyFormatter.format(p.getPrice() * amount)%></th>
                </tr>
                <% productCount++; %>
            <% } %>
        </table>
    </section>
<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>