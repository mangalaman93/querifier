<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/simpletree.css" />
        <title>Querifier</title>
        <script type="text/javascript">
            //if already logged in
            <%
                if (session.getAttribute("loggedin") != null && session.getAttribute("loggedin") == "true") {
                    response.sendRedirect("ServiceLoginAuth");
                }
            %>
                //function to validate the form
                function validateForm(){
                    var usrname = document.forms["login"]["username"].value;
                    var passwd = document.forms["login"]["password"].value;
                    if(usrname == "" ||  usrname == null) {
                        document.getElementById("error").innerHTML="Username field is blank";
                        return false;
                    } else if (passwd == "" || passwd == null) {
                        document.getElementById("error").innerHTML="Password field is blank";
                        return false;
                    }
                    return true;
                }
        </script>
    </head>
    <body>
        <div id="header" style="background-color: darkcyan"> 
            <p class="titleIndex">Querifier</p>
        </div>
        <div>
            <div id="lefthalf">
                <form action="ServiceLoginAuth"  name="login" method="post" onsubmit="return checkAuthentication()">
                    <table>
                        <tr>
                            <td>Username</td>
                            <td><input name="username" type="text" size="20" id="usrname"></td>
                        </tr>
                        <tr>
                            <td>Password</td>
                            <td><input name="password" type="password" size="20" id="passwd"></td>
                        </tr>
                        <tr style="">
                            <td id="error" colspan="2"><%
                                if (session.getAttribute("loggedin") != null
                                        && session.getAttribute("loggedin").equals("invalid")
                                        && session.getAttribute("attemptNo") != null) {
                                    out.print("Invalid login, please try again");
                                }%>
                            </td>
                        </tr>
                    </table>
                    <input type="submit" value="Login">
                </form>
            </div>
            <div id="righthalf">
                <p style="color: darkBlue;
                   font-family: sans-serif;
                   font-size: 30px;
                   padding-left: 50px;
                   padding-top: 160px;">Welcome to Querifier's login page</p>
            </div>
        </div>
    </body>

</html>
