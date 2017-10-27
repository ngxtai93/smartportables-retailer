<%@include file = "../partials/header.jsp" %>

<div id="body">
    <section class="content">
        <br>

        <p><a href="<%=rootPath%>/account/product/add">Add product</a></p>
        <p><a href="<%=rootPath%>/account/product/update">Update existing product</a></p>
        <p><a href="<%=rootPath%>/account/product/delete">Delete product</a></p>

        <br>
        <h2>Customer function</h2>
        <p><a href="<%=rootPath%>/account/order">My Orders</a></p>
        
        <br>
        <h2>Generate Report</h2>
        <p><a href="<%=rootPath%>/account/report/inventory">Inventory Report</a></p>
        <p><a href="<%=rootPath%>/account/report/sales">Sales Report</a></p>
    </section>

<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>