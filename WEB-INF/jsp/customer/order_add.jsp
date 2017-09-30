<%@include file = "../partials/header.jsp" %>
<%@ page import = "java.util.*" %>
<%@ page import = "tai.Order, tai.Product, tai.User" %>

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
        
        <% User user = (User) session.getAttribute("user-queried"); %>
        <% if(user != null) { %>
            <p><b>Add product</b></p>
            <form method="get">
                <table>
                    <tr>
                        <th><b>Category: </b></th>
                        <td>
                            <select name="category">
                                <option value="none">--------</option>
                                <% for(Category cat: listCategory) { %>
                                    <option value="<%=cat.getId()%>"><%=cat.getName()%></option>
                                <% } %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <button id="login-button" name="action" value="choose-category" type="submit">
                                Next
                            </button>
                        </td>
                        <td>
                            <button id="login-button" name="action" value="finish-add" type="submit">
                                Finish adding
                            </button>
                        </td>
                    </tr>
                    <% if(request.getAttribute("recently-added") != null) { %>
                        <td>
                            <p><span class="required">Product added.</span</p>
                        </td>
                    <% } %>
                </table>    
            </form>

            <% Map<Integer, Product> listProduct =
                (Map<Integer, Product>) request.getAttribute("listProduct");%>
            <% if(listProduct != null) {
                if(listProduct.size() == 0) { %>
                    <p><span class="required">No product in this category.</span</p>   
                <% }
                else { %>
                    <form method="get">
                        <br>
                        <table>
                            <tr>
                                <th><b>Product: </b></th>
                                <td>
                                    <select name="product-id" id="product-id-select">
                                        <option value="none">--------</option>
                                        <% for(Map.Entry<Integer, Product> entry: listProduct.entrySet()) { %>
                                            <option value="<%=entry.getKey()%>">
                                                <%=entry.getValue().getName()%>
                                            </option>
                                        <% } %>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <th><b>Amount: </b></th>
                                <td>
                                    <input required name="product-amount" type="text" size="5">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <button id="login-button" name="action" value="add-product" type="submit">
                                        Add
                                    </button>
                                </td>
                            </tr>
                        </table>
                        <input type="hidden" name="category" value="<%=request.getAttribute("category")%>">
                    </form>
                <% }
            }
        } %>

        <% if(request.getAttribute("done-add-product") != null) { %>
            <% Map<Product, Integer> mapOrderProduct = (Map<Product, Integer>)session.getAttribute("order-product-list"); %>
            <% if(mapOrderProduct == null || mapOrderProduct.size() == 0) { %>
                <p><b>No product in the order.</b></p>
            <% }
            else { %>
                <form method="post">
                    <table>
                        <tr>
                            <th><label for="name">Full name:</label></th>
                            <td><input required name="name" type="text" size="50" maxlength="50"><br></td>
                        </tr>
                        <tr>
                            <th><label for="address">Address:</label></th>
                            <td><input required name="address" type="text" size="50" maxlength="50"><br></td>
                        </tr>
                        <tr>
                            <th><label for="city">City: </label></th>
                            <td><input required name="city" type="text" size="50" maxlength="50"><br></td>
                        </tr>
                        <tr>
                            <th><label for="state">State: </label></th>
                            <td><input required name="state" type="text" size="50" maxlength="50"><br></td>
                        </tr>
                        <tr>
                            <th><label for="zip">ZIP</label></th>
                            <td><input required name="zip" type="text" size="50" maxlength="50"><br></td>
                        </tr>
                        <tr>
                            <th><label for="phone">Phone number:</label></th>
                            <td><input required name="phone" type="text" size="50" maxlength="50"><br></td>
                        </tr>
                    </table>
                    <br><br>
                    <table>
                        <tr>
                            <th><label for="cc-num">Credit card number:</label></th>
                            <td><input required name="cc-num" type="text" size="50" maxlength="50"><br></td>
                        </tr>
                        <tr>
                            <th><label for="cc-exp">Expiration date(MM/YY):</label></th>
                            <td><input required name="cc-exp" type="text" size="5" maxlength="5"><br></td>
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