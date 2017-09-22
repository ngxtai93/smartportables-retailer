<%@include file = "../partials/header.jsp" %>

<div id="body">
    <section class="content">
        <br>
        <% String category = (String) request.getAttribute("current-category");
        if(category != null) { %>
            <p>Browsing category <%=category%></p>
        <% } %>

        
    </section>

<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>