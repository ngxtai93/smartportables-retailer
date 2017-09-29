<%@include file = "./partials/header.jsp" %>
<%@ page import = "java.time.LocalDate, java.time.format.DateTimeFormatter, tai.Order" %>

<div id="body">
    <section class="content">
        <br>
        <% String commandExecuted = (String) session.getAttribute("command-executed"); %>
        <% if(commandExecuted.equals("order-cancel")) { %>
			<p>Order cancel failed: Must be cancelled before at least 5 business days.</p>
			<p>
				<a href="<%=rootPath%>/account">Back to account management</a>
			</p>
        <% } %>
        

        <%session.removeAttribute("command-executed");%>

    </section>
<%@include file = "./partials/sidebar.jsp" %>
</div>
<%@include file = "./partials/footer.jsp" %>