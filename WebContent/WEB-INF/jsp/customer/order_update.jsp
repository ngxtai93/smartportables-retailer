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
                        <p><span class="required">User ID not exists.</span></p>   
                <% } %>
            </div>
        </form>
        
        <%
            User user = (User) session.getAttribute("user-queried");
            List<Order> listOrder = (List<Order>) session.getAttribute("list-order");
        %>
        <% if(user != null) { %>
            <form method="get">
                <table>
                    <tr>
                        <th>
                            Choose order to update: 
                        </th>
                        <td>
                            <select name="order-id">
                                <option value="none">--------</option>
                                <% if(listOrder != null) { 
                                    for(Order order: listOrder) { %>
                                        <option value="<%=order.getId()%>">
                                            Order #<%=order.getId()%> / Confirmation number <%=order.getConfirmNumber()%>
                                        </option>
                                    <% }
                                } %>
                            </select>
                        </td>
                    </tr>
                </table>
                <button id="login-button" type="submit" name="action" value="choose-order">
                    Next
                </button>
            </form>

            <% Order order = (Order) session.getAttribute("queried-order"); %>
            <% if(order != null) { %>

                <form method="post">
                    <table>
                        <tr>
                            <th><label for="name">Full name:</label></th>
                            <td><input required value="<%=order.getName()%>" name="name" type="text" size="50" maxlength="50"><br></td>
                        </tr>
                        <tr>
                            <th><label for="address">Address:</label></th>
                            <td><input required value="<%=order.getAddress()%>" name="address" type="text" size="50" maxlength="50"><br></td>
                        </tr>
                        <tr>
                            <th><label for="city">City: </label></th>
                            <td><input required value="<%=order.getCity()%>" name="city" type="text" size="50" maxlength="50"><br></td>
                        </tr>
                        <tr>
                            <th><label for="state">State: </label></th>
                            <td><input required value="<%=order.getState()%>" name="state" type="text" size="50" maxlength="50"><br></td>
                        </tr>
                        <tr>
                            <th><label for="zip">ZIP</label></th>
                            <td><input required value="<%=order.getZip()%>" name="zip" type="text" size="50" maxlength="50"><br></td>
                        </tr>
                        <tr>
                            <th><label for="phone">Phone number:</label></th>
                            <td><input required value="<%=order.getPhone().toString()%>" name="phone" type="text" size="50" maxlength="50"><br></td>
                        </tr>
                    </table>
                    <br><br>
                    <table>
                        <tr>
                            <th><label for="cc-num">Credit card number:</label></th>
                            <td><input required value="<%=order.getCreditCardNum()%>" name="cc-num" type="text" size="50" maxlength="50"><br></td>
                        </tr>
                        <tr>
                            <th><label for="cc-exp">Expiration date(MM/YY):</label></th>
                            <td><input required value="<%=order.getShortExpDate()%>" name="cc-exp" type="text" size="5" maxlength="5"><br></td>
                        </tr>
                        <tr>
                            <th><label for="deliver-date">Delivery date(YYYY-MM-DD):</label></th>
                            <td><input required value="<%=order.getDeliverDate().toString()%>" name="deliver-date" type="text" size="10" maxlength="10"><br></td>
                        </tr>
                    </table>
                    <br><br>
                    <table>
                        <tr>
                            <th><label for="status">Status:</label></th>
                            <td>
                                <select name="status">
                                    <% String[] listStatus = (String[]) request.getAttribute("list-order-status");
                                    for(String status: listStatus) { %>
                                        <option <%=order.getStatus().equals(status) ? "selected" : ""%> value="<%=status%>">
                                            <%=status%>
                                        </option>
                                    <% } %>
                                </select>
                            </td>
                        </tr>
                    </table>
                    <button type="submit" id="login-button">
                        Submit
                    </button>
                </form>
            <% }
        } %>
        
    </section>

<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>