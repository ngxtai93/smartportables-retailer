<%@include file = "./partials/header.jsp" %>
<%@ page import = "java.util.Map" %>

<%
    Map<String, Double> listProductByRating = (Map<String, Double>) request.getAttribute("top-five-product-rating");
%>
<div id="body">
    <section class="content">
        <br>
        <h4>Top five product: </h4>
        <% for(Map.Entry<String, Double> entry: listProductByRating.entrySet()) { %>
            <span><%=entry.getKey()%>: Average rating of <%=entry.getValue()%>/5.0<br></span>
        <% } %>
    </section>
<%@include file = "./partials/sidebar.jsp" %>
</div>
<%@include file = "./partials/footer.jsp" %>