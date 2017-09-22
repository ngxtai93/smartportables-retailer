<%@ page import = "java.util.Map" %>
<%@ page import = "tai.Category, tai.Product" %>

<%@include file = "../partials/header.jsp" %>
<div id="body">
    <section class="content">
        <br>
        <%
            String currentCategory = (String) request.getAttribute("current-category");
            Map<Integer, Product> mapProduct = (Map<Integer, Product>) request.getAttribute("mapProduct");
            Category category = null;
            for(Category cat: listCategory) {
                if(cat.getId().equals(currentCategory)) {
                    category = cat;
                    break;
                }
            }
        %>

        <% if(category != null) { %>
            <h2>All <%=category.getName()%></h2>
            <hr>

            <%-- <% for(Map.Entry<Integer, Product> entry: mapProduct.entrySet()) { %>
                <% Integer productId = entry.getKey();%>
                
                <% request.setAttribute("current-product-id", productId); %>
                <jsp:include page="/WEB-INF/jsp/partials/product_detail.jsp"/>
            <% } --%>
            <% request.setAttribute("current-product-id", new Integer(1)); %>
            <jsp:include page="/WEB-INF/jsp/partials/product_detail.jsp"/>
        <% } %>

        
    </section>

<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>