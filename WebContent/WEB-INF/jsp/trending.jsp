<%@include file = "./partials/header.jsp" %>
<%@ page import = "java.util.Map" %>
<%@ page import = "tai.utils.StringUtilities" %>

<%
    Map<String, Double> listProductByRating = (Map<String, Double>) request.getAttribute("top-five-product-rating");
    Map<Integer, Integer> listZipByProduct = (Map<Integer, Integer>) request.getAttribute("top-five-zip");
    Map<String, Integer> listProductByAmount = (Map<String, Integer>) request.getAttribute("top-five-product-amount");
    StringUtilities stringUtil = StringUtilities.INSTANCE;
%>
<div id="body">
    <section class="content">
        <br>
        <h4>Top five product: </h4>
        <% for(Map.Entry<String, Double> entry: listProductByRating.entrySet()) { %>
            <span><%=stringUtil.filter(entry.getKey())%>: Average rating of <%=entry.getValue()%>/5.0<br></span>
        <% } %>
        <br>
        <h4>Top five zip: </h4>
        <% for(Map.Entry<Integer, Integer> entry: listZipByProduct.entrySet()) { %>
            <span><%=entry.getKey()%>: <%=entry.getValue()%> products<br></span>
        <% } %>
        <br>
        <h4>Top five most sold product: </h4>
        <% for(Map.Entry<String, Integer> entry: listProductByAmount.entrySet()) { %>
            <span><%=stringUtil.filter(entry.getKey())%>: <%=entry.getValue()%> sold<br></span>
        <% } %>
    </section>
<%@include file = "./partials/sidebar.jsp" %>
</div>
<%@include file = "./partials/footer.jsp" %>