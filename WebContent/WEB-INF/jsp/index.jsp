<%@ page import = "tai.entity.Deal, tai.entity.Product" %>
<%@ page import = "java.util.List, java.util.Map" %>
<%@include file = "./partials/header.jsp" %>

<%
	Map<Deal, Product> mapDealProduct = (Map<Deal, Product>) application.getAttribute("map-deal");
	Boolean isFormatted = (Boolean) application.getAttribute("is-formatted");
	if(mapDealProduct != null && mapDealProduct.size() > 0 && isFormatted == null) {
		for(Map.Entry<Deal, Product> entry: mapDealProduct.entrySet()) {
			Deal d = entry.getKey();
			String formattedHtml = "<a href=\"" + d.getLink() + "\">" + d.getLink() + "</a>";
			d.setTweet(d.getTweet().replaceAll(d.getLink(), formattedHtml));
		}
		application.setAttribute("is-formatted", Boolean.TRUE);
	}
%>
<div id="body">
    <section class="content">

    <article>
    	<h2>Best Buy Deals of the Day</h2>
    	<% if(mapDealProduct == null || mapDealProduct.size() == 0) { %>
    		<span>No offer found.</span>
    	<% }
    	else {
    		for(Map.Entry<Deal, Product> entry: mapDealProduct.entrySet()) { %>
    			<span><%=entry.getKey().getTweet()%></span>
    			<br>
    		<% }
    	} %>
    	<br>
    	<h2>Deal Match</h2>
    	<% if(mapDealProduct == null || mapDealProduct.size() == 0) { %>
    		<span>No offer found.</span>
    	<% }
    	else { 
    		for(Map.Entry<Deal, Product> entry: mapDealProduct.entrySet()) { %>
    			<span>
    				<a href="<%=rootPath%>/product/<%=entry.getValue().getCategory()%>/<%=entry.getValue().getId()%>"><%=entry.getValue().getName() %>
    				</a>
   				</span>
   			<% }
   		} %>
    			
    	<br>
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
