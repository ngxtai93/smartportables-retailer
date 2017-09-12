<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>SmartPortables</title>
    <link rel="stylesheet" href="resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="resources/css/styles.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
    <% String rootPath = request.getContextPath(); %>
<header>
    <nav class="navbar navbar-inverse navbar-fixed-top" id="header-bar">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="<%=rootPath%>">SmartPortables</a>
            </div>
            <div class="collapse navbar-collapse">
            <!--
                <ul class="nav navbar-nav">                    
                    <li class="active"><a href="/csj/">Home</a></li>
                    <li><a href="#about">About</a></li>
                    <li><a href="#contact">Contact</a></li>
                </ul>
            -->
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <a href="<%=rootPath%>/login">
                            <i class="glyphicon glyphicon-log-in"></i>
                            Login
                        </a>
                    </li>
                    <li>
                        <a href="<%=rootPath%>/register">
                            <span class="glyphicon glyphicon-user"></span>
                            Register
                        </a>
                    </li>
                    <li>
                        <a href="<%=rootPath%>/cart">
                            <i class="glyphicon glyphicon-shopping-cart"></i>
                        </a>
                    </li>
                </li>
            </div><!--/.nav-collapse -->
        </div>
    </nav>
</header>