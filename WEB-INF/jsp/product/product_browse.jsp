<%@include file = "../partials/header.jsp" %>

<div id="body">
    <section class="content">
        <br>
        <%
            String currentCategory = (String) request.getAttribute("current-category");
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
        <% } %>

        
    </section>

<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>