<%@ page import="java.time.LocalDate, java.time.format.DateTimeFormatter" %> 
<%@ page import="tai.entity.User" %> 
<%@ include file = "../partials/header.jsp" %>

<% DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu"); %>

<div id="body">
    <section class="content">
        <br>
        <h2><b>Review a Product</b></h2>
        <form method="post">
            <table>
                <tr>
                    <th><b>Product model name:</b></th>
                    <td>
                        <input required name="product-name" type="text">
                    </td>
                </tr>
                <tr>
                    <th><b>Product category: </b></th>
                    <td>
                        <input name="product-category" type="text">
                    </td>
                </tr>
                <tr>
                    <th><b>Product price:</b></th>
                    <td>
                        <input name="product-price" type="text">
                    </td>
                </tr>
                <tr>
                    <th><b>Retailer name:</b></th>
                    <td>
                        <input name="retailer-name" type="text">
                    </td>
                </tr>
                <tr>
                    <th><b>Retailer zip:</b></th>
                    <td>
                        <input name="retailer-zip" type="text">
                    </td>
                </tr>
                <tr>
                    <th><b>Retailer city:</b></th>
                    <td>
                        <input name="retailer-city" type="text">
                    </td>
                </tr>
                <tr>
                    <th><b>Retailer state:</b></th>
                    <td>
                        <input name="retailer-state" type="text">
                    </td>
                </tr>
                <tr>
                    <th><b>Product on sale:</b></th>
                    <td>
                        <input name="product-on-sale" type="checkbox">
                    </td>
                </tr>
                <tr>
                    <th><b>Manufacturer name:</b></th>
                    <td>
                        <input name="manufacturer-name" type="text">
                    </td>
                </tr>
                <tr>
                    <th><b>Manufacturer rebate:</b></th>
                    <td>
                        <input name="manufacturer-rebate" type="checkbox">
                    </td>
                </tr>
                <tr>
                    <th><b>User ID:</b></th>
                    <td>
                        <input required name="user-id" value="<%=currentUser.getUsername()%>" type="text">
                    </td>
                </tr>
                <tr>
                    <th><b>Age: </b></th>
                    <td>
                        <input required name="user-age" type="text">
                    </td>
                </tr>
                <tr>
                    <th><b>Gender:</b></th>
                    <td>
                        <select name="gender">
                            <option value="male">Male</option>
                            <option value="female">Female</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th><b>Occupation:</b></th>
                    <td>
                        <input required name="occupation" type="text">
                    </td>
                </tr>
                <tr>
                    <th><b>Review rating:</b></th>
                    <td>
                        <input required name="review-rating" type="text" size="1">/5
                    </td>
                </tr>
                <tr>
                    <th><b>Review date:</b></th>
                    <td>
                        <input required name="product-name" type="text" value="<%=formatter.format(LocalDate.now())%>">
                    </td>
                </tr>
                <tr>
                    <th><b>Review text:</b></th>
                    <td>
                        <textarea required row="10" name="review-text"></textarea>
                    </td>
                </tr>                                                                                                                                                                                                                                                
            </table>
            <button name="login-button" type="submit">Submit</button>
        </form>        
    </section>

<%@include file = "../partials/sidebar.jsp" %>
</div>
<%@include file = "../partials/footer.jsp" %>