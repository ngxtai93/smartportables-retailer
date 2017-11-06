<%@include file = "../partials/header.jsp" %>
<%@ page import = "java.util.*" %>
<%@ page import = "tai.entity.Product" %>

<div id="body">
    <section class="content">
        <h4><a href="<%=rootPath%>/account/report/sales/list">1. Sales list</a></h4>
        <h4><a href="<%=rootPath%>/account/report/sales/barchart">2. Sales bar chart</a></h4>
        <h4><a href="<%=rootPath%>/account/report/sales/daily">3. Total daily sales</a></h4>
    </section>
<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>