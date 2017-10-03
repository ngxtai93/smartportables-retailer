<%@include file = "../partials/header.jsp" %>
<%@ page import = "java.util.*" %>
<%@ page import = "tai.entity.Order, tai.entity.Product, tai.entity.User" %>

<div id="body">
    <section class="content">
        <br>
        <form method="get">
            <div class="container">
                <br>
                <table>
                    <tr>
                        <th><b>Enter customer's user name: </b></th>
                        <td>
                           <input type="text" size="20" name="username">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button id="login-button" type="submit" name="action" value="choose-user">
                                Next
                            </button>
                        </td>
                    </tr>
                </table>
                <% String addFailed = (String)request.getAttribute("addFailed"); %>
                <% if(addFailed != null && addFailed.equals("username")) { %>
                        <p><span class="required">User ID not exists.</span</p>   
                <% } %>
            </div>
        </form>
        
        <%
            User user = (User) session.getAttribute("user-queried");
            List<Order> listOrder = (List<Order>) session.getAttribute("list-order");
        %>
        <% if(user != null) { %>
            <form method="post">
                <table>
                    <tr>
                        <th>
                            Choose order to delete: 
                        </th>
                        <td>
                            <select name="order-id">
                                <option value="none">--------</option>
                                <% for(Order order: listOrder) { %>
                                    <option value="<%=order.getId()%>">
                                        Order #<%=order.getId()%> / Confirmation number <%=order.getConfirmNumber()%>
                                    </option>
                                <% } %>
                            </select>
                        </td>
                    </tr>
                </table>
                <button id="login-button" type="submit" name="action" value="delete-order">
                    Submit
                </button>
            </form>
        <% } %>
        
    </section>

<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>