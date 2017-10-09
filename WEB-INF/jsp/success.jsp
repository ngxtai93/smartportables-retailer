<%@include file = "./partials/header.jsp" %>
<%@ page import = "java.time.LocalDate, java.time.format.DateTimeFormatter, tai.entity.Order" %>

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
		<% if(commandExecuted.equals("order-place")) {
			Order order = (Order) session.getAttribute("order");
			LocalDate deliverDate = order.getDeliverDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, uuuu");
			session.removeAttribute("order");
		%>
			<p>Thank you for shopping with us. Your order has been successfully placed. </p>
			<p>Your order is scheduled to be shipped on <%=formatter.format(deliverDate)%>. </p>
			<p>Your confirmation number is <%= order.getConfirmNumber() %>.</p>
			<p>
				<a href="<%=rootPath%>">Back to homepage</>
			</p>
        <% } %>
		<% if(commandExecuted.equals("order-cancel")) {
		%>
			<p>Order successfully cancelled.</p>
			<p>
				<a href="<%=rootPath%>/account">Back to your account</>
			</p>
        <% } %>
		<% if(commandExecuted.equals("sales-order-add")) {
		%>
			<p>Order successfully placed.</p>
			<p>
				<a href="<%=rootPath%>/account">Back to your account</>
			</p>
        <% } %>
		<% if(commandExecuted.equals("sales-order-update")) {
		%>
			<p>Order successfully updated.</p>
			<p>
				<a href="<%=rootPath%>/account">Back to your account</>
			</p>
        <% } %>
		<% if(commandExecuted.equals("sales-order-delete")) {
		%>
			<p>Order successfully deleted.</p>
			<p>
				<a href="<%=rootPath%>/account">Back to your account</>
			</p>
        <% } %>
		<% if(commandExecuted.equals("product-review")) { %>
			<p>Review submitted.</p>
			<p>
				<a href="<%=rootPath%>">Back to homepage</>
			</p>
        <% } %>
        <%session.removeAttribute("command-executed");%>

    </section>
<%@include file = "./partials/sidebar.jsp" %>
</div>
<%@include file = "./partials/footer.jsp" %>