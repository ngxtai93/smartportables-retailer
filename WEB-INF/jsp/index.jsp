<%@include file = "./partials/header.jsp" %>
<div id="body">
    <section class="content">

    <article>
        <h2>Available Products</h2>
        <hr>
        
        <div class="overview-all-items">
            <% if(listCategory != null) { %>
                <% for(Category cat: listCategory) { %>
                    <%-- not show accessory --%>
                    <% if(!cat.getId().equals("accessory")) { %>
                        <div class="overview-item">
                            <a href="<%=rootPath%>/product/<%=cat.getId()%>">
                                <div class="overview-item-image">
                                    <img src="<%=rootPath%>/resources/images/product/<%=cat.getId()%>/<%=cat.getImageName()%>" height="250" width="250">
                                </div>
                                <div class="overview-item-caption">
                                    <%=cat.getName()%>
                                </div>
                            </a>
                        </div>
                    <% } %>
                <% } %>
            <% } %>
        </div>
    </article>
    </section>
    
<%@include file = "./partials/sidebar.jsp" %>
</div>

<%@include file = "./partials/footer.jsp" %>
