<%@ page import = "tai.User, tai.Role" %>
<%@ page import = "java.util.ArrayList" %>
<%@ page import = "tai.Category" %>

<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>SmartPortables</title>
    <link rel="stylesheet" href="resources/css/styles.css" type="text/css" />
</head>
<body>

<% String rootPath = request.getContextPath(); %>
<%-- Get list of category  --%>
<% request.getRequestDispatcher("getListCategory").include(request, response); %>
<% ArrayList<Category> listCategory = (ArrayList<Category>) request.getAttribute("listCategory"); %>

<div id="container">
    <header>
    	<h1><a href="<%=rootPath%>">Smart<span>Portables</span></a></h1>
    </header>
    <nav>
    	<ul>
        	<li class="start selected"><a href="<%=rootPath%>">Home</a></li>
            <%-- <li class=""><a href="examples.html">Examples</a></li> --%>
            <%  User currentUser = (User) session.getAttribute("currentUser"); %>
            <%  if(currentUser == null) { %>
                    <li><a href="login">Login</a></li>
                    <li><a href="register">Register</a></li>"
            <%  }
                else { %>
                    <li><a href="account">My Account</a></li>
                    <li><a href="logout">Sign Out</a></li>
            <%  } %>
                            
            <%-- <li class="end"><a href="#">Contact</a></li> --%>
        </ul>
    </nav>

	<img class="header-image" src="resources/images/consumer_electronics_accessories.jpg" alt="Devices" />