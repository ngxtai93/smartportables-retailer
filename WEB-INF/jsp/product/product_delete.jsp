<%@include file = "../partials/header.jsp" %>
<%@ page import = "java.util.Map" %>
<%@ page import = "tai.entity.Product" %>
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
                    <form method="post" action="<%=rootPath%>/deleteProduct">
                    <input type="hidden" name="category" value="<%=categoryParameter%>">
                    <div class="container">
                        <% for(Map.Entry<Integer, Product> entry: mapProductByCategory.entrySet()) { %>
                            <input type="checkbox" name="product-id" value="<%=entry.getKey()%>">
                            <%=entry.getValue().getName()%><br>
                        <% } %>
                        <button id="login-button" type="submit">Submit</button>
                    </div>
                </form>
            <% } 
                if(mapProductByCategory.size() == 0) { %>
                    <p><strong>Category <%=category.getName()%> has no product.</strong></p>
            <% } 
        } %>
        <% if(request.getAttribute("no-product-chosen") != null) { %>
                    <p style="color: red"><strong>Please choose product to delete.</strong></p>
                    <% request.removeAttribute("no-product-chosen");
        } %> 

    </section>

<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>