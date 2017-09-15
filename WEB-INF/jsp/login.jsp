<%@include file = "./partials/header.jsp" %>

<div id="body">
    <section id="content">
        <form method="post">
            <div class="container">
                <br>
                <table>
                    <tr>
                        <th class="login-header"><b>Username</b></th>
                        <td><input type="text" placeholder="Enter Username" name="username" required></td>
                    </tr>
                    <tr>
                        <th class="login-header"><b>Password</b></th>
                        <td><input type="password" placeholder="Enter Password" name="password" required></th>
                    </tr>
                    <tr>
                        <td><button id="login-button" type="submit">Login</button></td>
                    </tr>
                </table>
            </div>
        </form>
        <%
            String loginFailed = (String)request.getAttribute("loginFailed");
            if(loginFailed != null) {
                if(loginFailed.equals("username") || loginFailed.equals("password")) {
                    out.println("<p><span class=\"required\">Username or password is not correct</span</p>");
                }    
            }
        %>
    </section>
<%@include file = "./partials/sidebar.jsp" %>
</div>
<%@include file = "./partials/footer.jsp" %>