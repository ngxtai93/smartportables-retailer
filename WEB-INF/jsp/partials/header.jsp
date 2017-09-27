<%@ page import = "tai.User, tai.Role" %>
<%@ page import = "java.util.ArrayList" %>
<%@ page import = "tai.Category" %>

<% String rootPath = request.getContextPath(); %>

<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>SmartPortables</title>
    <link rel="stylesheet" href="<%=rootPath%>/resources/css/styles.css" type="text/css" />
</head>
<body>


<%-- Get list of category  --%>
<% ArrayList<Category> listCategory = (ArrayList<Category>) request.getAttribute("listCategory"); %>

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
                else { %>
                    <li><a href="<%=rootPath%>/account">My Account</a></li>
                    <li><a href="<%=rootPath%>/logout">Sign Out</a></li>
                    <li><a href="<%=rootPath%>/cart">Cart(<%=currentUser.getShoppingCart().countItem()%>)</a></li>
            <%  } %>
                            
            <%-- <li class="end"><a href="#">Contact</a></li> --%>
        </ul>
    </nav>

	<img class="header-image" src="<%=rootPath%>/resources/images/consumer_electronics_accessories.jpg" alt="Devices" />