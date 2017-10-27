<%@include file = "../partials/header.jsp" %>
<%@ page import = "java.util.*" %>
<%@ page import = "tai.entity.Product" %>

<% List<Product> listAllProduct = (List<Product>) request.getAttribute("listAllProduct"); %>
<% int productCount = 1; %>
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
            <% for(Product p: listAllProduct) {
                if(p.getRebate() != null && Double.compare(p.getRebate().doubleValue(), 0.0) > 0) { %>     
                    <tr>
                        <th><%=productCount%></th>
                        <th><%=p.getName()%></th>
                        <th><%=p.getPrice()%></th>
                        <th><%=p.getAmount()%></th>
                    </tr>
                    <% productCount++; %>
                <% }
            } %>
        </table>
    </section>
<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>