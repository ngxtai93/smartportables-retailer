<%@include file = "../partials/header.jsp" %>
<%@ page import = "java.util.Map" %>
<%@ page import = "tai.entity.Product" %>

<% Map<Integer, Product> mapAccessory = (Map<Integer, Product>) request.getAttribute("list-accessory"); %>
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
                            <select name="category">
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
        <% String categoryParameter = (String) request.getParameter("category");
            if(categoryParameter != null && !categoryParameter.equals("none")) { %>
            <form method="post" action="<%=rootPath%>/addProduct" enctype="multipart/form-data">
                <input type="hidden" name="category" value="<%=categoryParameter%>">
                <div class="container">
                    <br>
                    <table>
                        <tr>
                            <th><b>Product name:</b></th>
                            <td><input required name="name" type="text" size="40"></td>
                        </tr>
                        <tr>
                            <th><b>Original price: </b></th>
                            <td><input required name="price" type="text" size="10"></td>
                        </tr>
                        <tr>
                            <th><b>Discount: </b></th>
                            <td><input name="discount" type="text" size="10"></td>
                        </tr>
                        <tr>
                            <th><b>Rebate: </b></th>
                            <td><input name="rebate" type="text" size="10"></td>
                        </tr>
                        <tr>
                            <th><b>Image: </b></th>
                            <td><input required name="image" type="file" size="30"></td>
                        </tr>
                    </table>
                    
                    <div class="container">
                        <% if(mapAccessory != null && mapAccessory.size() != 0) { %>
                            <b>Include accessories: </b>
                            <br>
                            <% for(Map.Entry<Integer, Product> entry: mapAccessory.entrySet()) { %>
                                <input type="checkbox" name="accessory-id" value="<%=entry.getKey()%>">
                                <%=entry.getValue().getName()%><br>
                            <% }
                        } %>
                    </div>
                    
                    <button id="login-button" type="submit">Submit</button>   
                </div>
            </form>
        <% } %>
    </section>

<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>