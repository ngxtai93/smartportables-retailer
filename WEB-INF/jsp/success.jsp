<%@include file = "./partials/header.jsp" %>

<div id="body">
    <section class="content">
        <% String commandExecuted = (String) session.getAttribute("command-executed"); %>
        <% if(commandExecuted.equals("product-add")) { %>
                <p>Add product successful.</p>
        <% } %>
        <% if(commandExecuted.equals("product-update")) { %>
                <p>Update product successful.</p>
        <% } %>
        <% if(commandExecuted.equals("product-delete")) { %>
                <p>Delete product successful.</p>
        <% } %>

        <%session.removeAttribute("command-executed");%>
        <p>
            <a href="<%=rootPath%>/account">Back to account management</a>
        </p>
    </section>
<%@include file = "./partials/sidebar.jsp" %>
</div>
<%@include file = "./partials/footer.jsp" %>