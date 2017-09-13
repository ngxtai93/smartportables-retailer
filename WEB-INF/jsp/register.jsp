<%@include file = "./partials/header.jsp" %>

<div id="body">
    <section id="content">
        <form method="post">
            <div class="container">
                <br>
                <table>
                    <tr>
                        <th><b>Username</b></th>
                        <td><input type="text" placeholder="Enter Username" name="username" required></td>
                    </tr>
                    <tr>
                        <th><b>Password</b></th>
                        <td><input type="password" placeholder="Enter Password" name="password" required></th>
                    </tr>
                    <tr>
                        <td><button id="register-button" type="submit">Register</button></td>
                    </tr>
                </table>
            </div>
        </form>
    </section>

<%@include file = "./partials/sidebar.jsp" %>
</div>
<%@include file = "./partials/footer.jsp" %>