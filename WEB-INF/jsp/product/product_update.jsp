<%@include file = "../partials/header.jsp" %>
<%@ page import = "java.util.Map" %>
<%@ page import = "tai.Product" %>
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
        <% String categoryParameter = (String) request.getParameter("category"); %>
        <%  Category category = null;
            if(categoryParameter != null) {
                for(Category cat: listCategory) {
                    if(cat.getId().equals(categoryParameter)) {
                        category = cat;
                        break;
                    }
                }
            }
        %>

        <% Map<Integer, Product> mapProductByCategory = (Map<Integer, Product>) request.getAttribute("mapProduct"); %>
        <% if(category != null) { 
                if(mapProductByCategory.size() > 0) { %>
                    <form method="post" action="<%=rootPath%>/updateProduct" enctype="multipart/form-data">
                    <input type="hidden" name="category" value="<%=categoryParameter%>">
                    <div class="container">
                        <% for(Map.Entry<Integer, Product> entry: mapProductByCategory.entrySet()) { %>
                            <input required type="radio" name="product-id" value="<%=entry.getKey()%>">
                            <%=entry.getValue().getName()%><br>
                        <% } %>
                    </div>
                    <div class="container">
                        <br>
                        <table>
                            <tr>
                                <th><b>Product name:</b></th>
                                <td><input name="name" type="text" size="40"></td>
                            </tr>
                            <tr>
                                <th><b>Original price: </b></th>
                                <td><input name="price" type="text" size="10"></td>
                            </tr>
                            <tr>
                                <th><b>Discount: </b></th>
                                <td><input name="discount" type="text" size="10"></td>
                            </tr>
                            <tr>
                                <th><b>Image: </b></th>
                                <td><input name="image" type="file" size="30"></td>
                            </tr>
                            <tr>
                                <td><button id="login-button" type="submit">Submit</button></td>
                            </tr>
                        </table>
                    </div>
                </form>
            <% } 
                if(mapProductByCategory.size() == 0) { %>
                    <p><strong>Category <%=category.getName()%> has no product.</strong></p>
            <% } 
        } %>
    </section>

<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>