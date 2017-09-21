<%@include file = "./partials/header.jsp" %>

<div id="body">
    <section class="content">
        <br>
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
        <% if(commandExecuted.equals("sale-customer-register")) { %>
                <p>Customer account successfully created.</p>
        <% } %>
        <p>
        	<a href="<%=rootPath%>/account">Back to account management</a>
        </p>
        <% if(commandExecuted.equals("product-add")) { %>
        	<a href="<%=rootPath%>/account/product/add">Add more product</a>
        <% } %>

        <%session.removeAttribute("command-executed");%>

    </section>
<%@include file = "./partials/sidebar.jsp" %>
</div>
<%@include file = "./partials/footer.jsp" %>