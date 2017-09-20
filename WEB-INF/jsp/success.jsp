<%@include file = "./partials/header.jsp" %>

<div id="body">
    <section class="content">
        <%if(session.getAttribute("command-executed").equals("product-add")) { %>
            <p>Add product successful.</p>
            <% session.removeAttribute("command-executed"); %>
        <% } %>
        
        <p>
            <a href="<%=rootPath%>/account">Back to account management</a>
        </p>
    </section>
<%@include file = "./partials/sidebar.jsp" %>
</div>
<%@include file = "./partials/footer.jsp" %>