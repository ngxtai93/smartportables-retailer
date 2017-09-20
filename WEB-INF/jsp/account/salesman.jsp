<%@include file = "../partials/header.jsp" %>

<div id="body">
    <section class="content">
        <br>
        <p><a href="<%=rootPath%>/account/customer/register">Create customer account</a></p>
        <p><a href="<%=rootPath%>/account/customer/order/add">Add customer order</a></p>
        <p><a href="<%=rootPath%>/account/customer/order/update">Update customer order</a></p>
        <p><a href="<%=rootPath%>/account/customer/order/delete">Delete customer order</a></p>
    </section>

<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>