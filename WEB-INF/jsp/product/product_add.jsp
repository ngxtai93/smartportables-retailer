<%@include file = "../partials/header.jsp" %>

<div id="body">
    <section class="content">
        <br>
         <form method="get" name="category-form">
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
    </section>

<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>