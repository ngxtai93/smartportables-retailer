<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>SmartPortables</title>
    <link rel="stylesheet" href="resources/css/styles.css" type="text/css" />
</head>
<body>

<% String rootPath = request.getContextPath(); %>

<div id="container">
    <header>
    	<h1><a href="<%=rootPath%>">Smart<span>Portables</span></a></h1>
    </header>
    <nav>
    	<ul>
        	<li class="start selected"><a href="<%=rootPath%>">Home</a></li>
            <%-- <li class=""><a href="examples.html">Examples</a></li> --%>
            <%  String user = (String) request.getAttribute("username");
                if(user == null || user.length() == 0) {
                    out.println("<li><a href=\"login\">Login</a></li>");
                    out.println("<li><a href=\"register\">Register</a></li>");
                }
                else {
                    out.println("<li><a href=\"account\">My Account</a></li>");
                    out.println("<li><a href=\"logout\">Sign Out</a></li>");
                }
            %>
            <%-- <li class="end"><a href="#">Contact</a></li> --%>
        </ul>
    </nav>

	<img class="header-image" src="resources/images/consumer_electronics_accessories.jpg" alt="Devices" />