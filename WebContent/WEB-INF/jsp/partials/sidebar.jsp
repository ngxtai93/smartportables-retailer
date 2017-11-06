    <aside class="sidebar">

        <ul>	
            <li>
                <h4>Browse</h4>
                <ul>
                    <% if(listCategory != null) { %>
                        <% for(Category cat: listCategory) { %>
                            <%-- not show accessory --%>
                            <%if(!cat.getId().equals("accessory")) { %>
                                <li>
                                    <a href="<%=rootPath%>/product/<%=cat.getId()%>"><%=cat.getName()%></a>
                                </li>
                            <% } %>
                        <% } %>
                    <% } %>
                </ul>
            </li>
            <li>
                <h4><a href="<%=rootPath%>/trending">Trending</a></h4>
            </li>
        </ul>
    
    </aside>
    <div class="clear"></div>