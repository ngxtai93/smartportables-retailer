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
                <h4>About us</h4>
                <ul>
                    <li class="text">
                        <p style="margin: 0;">Aenean nec massa a tortor auctor sodales sed a dolor. Duis vitae lorem sem. Proin at velit vel arcu pretium luctus. 					<a href="#" class="readmore">Read More &raquo;</a></p>
                    </li>
                </ul>
            </li>
            
            <li>
                <h4>Search site</h4>
                <ul>
                    <li class="text">
                        <form method="get" class="searchform" action="#" >
                            <p>
                                <input type="text" size="30" value="" name="s" class="s" />
                                
                            </p>
                        </form>	
                    </li>
                </ul>
            </li>
            
            <li>
                <h4>Helpful Links</h4>
                <ul>
                    <li><a href="http://www.themeforest.net/?ref=spykawg" title="premium templates">Premium HTML web templates from $10</a></li>
                    <li><a href="http://www.dreamhost.com/r.cgi?259541" title="web hosting">Cheap web hosting from Dreamhost</a></li>
                    <li><a href="http://tuxthemes.com" title="Tux Themes">Premium WordPress themes</a></li>
                </ul>
            </li>
            
        </ul>
    
    </aside>
    <div class="clear"></div>