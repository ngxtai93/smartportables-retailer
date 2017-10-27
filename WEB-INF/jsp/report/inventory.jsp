<%@include file = "../partials/header.jsp" %>
<%@ page import = "java.util.*" %>
<%@ page import = "tai.entity.Product" %>

<div id="body">
    <section class="content">
        <h4><a href="<%=rootPath%>/account/report/inventory/list">1. List all Product</a></h4>
        <h4><a href="<%=rootPath%>/account/report/inventory/barchart">2. Bar Chart</a></h4>
    </section>
<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>