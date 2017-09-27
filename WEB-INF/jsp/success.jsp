<%@include file = "./partials/header.jsp" %>

<div id="body">
    <section class="content">
        <br>
        <% String commandExecuted = (String) session.getAttribute("command-executed"); %>
        <% if(commandExecuted.equals("product-add")) { %>
			<p>Add product successful.</p>
			<p>
				<a href="<%=rootPath%>/account">Back to account management</a>
				<a href="<%=rootPath%>/account/product/add">Add more product</a>
			</p>
        <% } %>
        <% if(commandExecuted.equals("product-update")) { %>
			<p>Update product successful.</p>
			<p>
				<a href="<%=rootPath%>/account">Back to account management</a>
			</p>
        <% } %>
        <% if(commandExecuted.equals("product-delete")) { %>
			<p>Delete product successful.</p>
			<p>
				<a href="<%=rootPath%>/account">Back to account management</a>
			</p>
        <% } %>
        <% if(commandExecuted.equals("sale-customer-register")) { %>
			<p>Customer account successfully created.</p>
        <% } %>

        <% if(commandExecuted.equals("cart-add")) {
			String prevUri = (String) session.getAttribute("prev-uri");
			session.removeAttribute("prev-uri");
		%>
			<p>Product added to cart.</p>
			<p>
				<a href="<%=prevUri%>">Back to shopping</>
			</p>
        <% } %>


        <%session.removeAttribute("command-executed");%>

    </section>
<%@include file = "./partials/sidebar.jsp" %>
</div>
<%@include file = "./partials/footer.jsp" %>