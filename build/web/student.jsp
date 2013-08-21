<%@page import="javax.swing.text.Document"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/simpletree.css" />
        <script type='text/javascript' src='js/student.js'></script>
        <title>Querifier</title>
    </head>
    <body>
        <div id="header" style="background-color: darkcyan"> 
            <a style="float: right; padding-right: 10px; padding-top: 10px; font-size: large; color: indigo;" href="logout"> Logout </a>
            <p class="titleIndex">Querifier</p>
        </div>
        <div>
            <div id="leftpane">
                <%@page import="javax.servlet.http.HttpSession" %>
                <a style="padding-left: 14px;" href="javascript:ddtreemenu.flatten('treemenu1', 'expand')">Expand All</a> | <a href="javascript:ddtreemenu.flatten('treemenu1', 'contact')">Contract All</a>
                <ul id="treemenu1" class="treeview">
                    <% out.print("<li onclick=\"getHomeStudent('" + ("" + session.getAttribute("name")).toUpperCase() +"')\">Home</li>"); %>
                    <li onclick="getNewsForum()"> NewsForum </li>
                    <li onclick="getDatabases()"> Databases </li>
                    <%
                        int numasgn = Integer.parseInt("" + session.getAttribute("numasgn"));
                        for (int i = 1; i <= numasgn; i++) {
                            out.print("<li> <a id=\"treelink\" href=\"javascript:getAsgn(" + i + ");\"> Assignment " + i + "</a>");
                            int q = Integer.parseInt("" + session.getAttribute("asgn" + i + "q"));
                            out.print("<ul>");
                            for (int j = 1; j <= q; j++) {
                                out.print("<li onclick=\"getQuestion(" + i + "," + j + ")\"> Question " + j + " </li>");
                            }
                            out.print("</ul></li>");
                        }
                    %>
                </ul>
                <script type="text/javascript">
                    ddtreemenu.createTree("treemenu1", true)
                </script>
            </div>

            <div  id="centerpane" style="text-align: center; color: darkblue;">
                <h1> WELCOME <% out.print(("" + session.getAttribute("name")).toUpperCase());%>!</h1>
            </div>
            <div id="rightpane">
                <h2 style="text-decoration: underline; color: indigo;">Updates</h2>
                <div id="rpanesub">
                </div>
            </div>
        </div>
        <script type="text/javascript">
            getUpdates();
            document.getElementById("centerpane").style.backgroundImage = "url(../images/spinner.png)"; // no-repeat center center';
        </script>
    </body>
</html>

