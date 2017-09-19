<%@include file = "../partials/header.jsp" %>

<div id="body">
    <section class="content">
        <br>
         <form method="get">
            <div class="container">
                <br>
                <table>
                    <tr>
                        <th><b>Category: </b></th>
                        <td>
                            <select name="category-option">
                                <option value="none">--------</option>
                                <% for(Category cat: listCategory) { %>
                                    <option value="<%=cat.getId()%>"><%=cat.getName()%></option>
                                <% } %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><button id="login-button" type="submit">Next</button></td>
                    </tr>
                </table>
            </div>
        </form>
        <%if(request.getParameter("category-option") != null) { %>
            <form method="get">
                <input type="hidden" name="category" value="<%=request.getParameter("category-option")%>">
                <div class="container">
                    <br>
                    <table>
                        <tr>
                            <th><b>Product name:</b></th>
                            <td><input required name="product-name"type="text" size="40"></td>
                        </tr>
                        <tr>
                            <th><b>Original price: </b></th>
                            <td><input required name="product-price"type="text" size="10"></td>
                        </tr>
                        <tr>
                            <th><b>Discount: </b></th>
                            <td><input name="product-discount"type="text" size="10"></td>
                        </tr>
                        <tr>
                            <td><button id="login-button" type="submit">Submit</button></td>
                        </tr>
                    </table>
                </div>
            </form>
        <% } %>
    </section>

<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>