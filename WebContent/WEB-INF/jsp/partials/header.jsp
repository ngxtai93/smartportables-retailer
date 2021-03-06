<%@ page import = "tai.entity.User, tai.entity.Role" %>
<%@ page import = "java.util.ArrayList" %>
<%@ page import = "tai.entity.Category" %>

<%  String rootPath = request.getContextPath();
    Boolean useBarchart = (Boolean) request.getAttribute("useBarchart");%>

<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>SmartPortables</title>
    <link rel="stylesheet" href="<%=rootPath%>/resources/css/styles.css" type="text/css" />
    <% if(useBarchart != null && useBarchart.equals(Boolean.TRUE)) { %>
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <% }%>
</head>
<body>



<%  ArrayList<Category> listCategory =
        (ArrayList<Category>) request.getAttribute("listCategory"); 
%>

<div id="container">
    <header>
    	<h1><a href="<%=rootPath%>">Smart<span>Portables</span></a></h1>
    </header>
    <nav>
    	<ul>
        	<li class="start"><a href="<%=rootPath%>">Home</a></li>
            <%-- <li class=""><a href="examples.html">Examples</a></li> --%>
            <%  User currentUser = (User) session.getAttribute("currentUser"); %>
            <%  if(currentUser == null) { %>
                    <li><a href="<%=rootPath%>/login">Login</a></li>
                    <li><a href="<%=rootPath%>/register">Register</a></li>"
            <%  }
                else {
                    int countItem = 0;
                    if(currentUser.getShoppingCart() != null) {
                        countItem = currentUser.getShoppingCart().countItem();
                    } %>
                    <li><a href="<%=rootPath%>/account">My Account</a></li>
                    <li><a href="<%=rootPath%>/logout">Sign Out</a></li>
                    <li><a href="<%=rootPath%>/cart">Cart(<%=countItem%>)</a></li>
                    <li><a href="<%=rootPath%>/review">Review a Product</a></li>
            <%  } %>
            <li>
            	<script src="<%=rootPath%>/resources/js/search.js" type="text/javascript"></script>
            	<div id="searchWrapper">
            		<form class="search-form" role="search" style="margin-top:17px">
            			<input 	name="value" id="searchInput" type="search" aria-label="Search" size="30" style="height:30px"
            					autocomplete="off" onkeyup="doAutoCompletion()">
            		</form>
           			<div id="live-search" style="margin-top: 23px;"></div>
            	</div>
           	</li>
                            
            <%-- <li class="end"><a href="#">Contact</a></li> --%>
        </ul>
    </nav>

	<img class="header-image" src="<%=rootPath%>/resources/images/consumer_electronics_accessories.jpg" alt="Devices" />