<%@ page import = "java.util.Map" %>
<%@ page import = "tai.Product" %>

<% 
    Map<Integer, Product> mapProduct = (Map<Integer, Product>) request.getAttribute("mapProduct");
    Product product =   mapProduct.get(
        (Integer) request.getAttribute("current-product-id")
    );
%>

<p>Current product id: <%=product.getId()%></p>
<p>Current product name: <%=product.getName()%></p>