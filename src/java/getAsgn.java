/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author aman
 */
@WebServlet(name = "getAsgn", urlPatterns = {"/getAsgn"})
public class getAsgn extends HttpServlet {

    private static final String SERVER = "localhost";
    private static final String DBNAME = "querifier";
    private static final String DB_USERNAME = "DB_project";
    private static final String DB_PASSWORD = "dbproject";

    //setting up connection to the server
    Connection connect() throws SQLException, Exception {
        Connection con = null;
        try {
            String url = "jdbc:mysql://" + SERVER + "/" + DBNAME + "?user=" + DB_USERNAME + "&password=" + DB_PASSWORD;
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url);
        } catch (SQLException sqle) {
            System.out.println("SQLException: Unable to open connection to db: " + sqle.getMessage());
            throw sqle;
        } catch (Exception e) {
            System.out.println("Exception: Unable to open connection to db: " + e.getMessage());
            throw e;
        }
        return con;
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedin") != null && session.getAttribute("loggedin").equals("true")) {
            int assgn_ID = Integer.parseInt("" + request.getParameter("ID"));
            Connection con = null;
            boolean isValid = false;
            try {
                con = connect();
                String LOGIN_QUERY = "select * from assignment where assgn_ID = ?";
                PreparedStatement prepStmt = con.prepareStatement(LOGIN_QUERY);
                prepStmt.setInt(1, assgn_ID);
                ResultSet rs = prepStmt.executeQuery();
                if (rs.next()) {
                    isValid = true;
                }
                if (isValid) {
                    String name = rs.getString("topic");
                    String mode = rs.getString("mode");
                    String deadline = ("" + rs.getTimestamp("deadline"));
                    String post_time = ("" + rs.getTimestamp("post_time"));
                    out.println("<h2>Assignment: " + name + "</h2>");
                    out.println("<table class=\"tables\" cellspacing=\"0\" border=\"1\" align=\"center\" width=\"60%\">");
                    out.println("<tr><td id=\"newsSubjectBackground\" style=\"font-size: large;\">Mode: </td><td>" + mode + "</td></tr>");
                    out.println("<tr><td id=\"newsSubjectBackground\" style=\"font-size: large;\">Deadline: </td><td>" + deadline + "</td></tr>");
                    out.println("<tr><td id=\"newsSubjectBackground\" style=\"font-size: large;\">Posted On: </td><td>" + post_time + "</td></tr>");
                    prepStmt = con.prepareStatement("SELECT grade from performance where ID=? and assgn_ID=? ");
                    int id;
                    id = Integer.parseInt("" + session.getAttribute("username"));
                    prepStmt.setInt(1, id);
                    prepStmt.setInt(2, assgn_ID);
                    rs = prepStmt.executeQuery();
                    if (rs.next()) {
                        out.println("<tr><td id=\"newsSubjectBackground\" style=\"font-size: large;\">Grade: </td><td>" + rs.getString("grade") + "</td></tr>");
                    } else {
                        out.println("<tr><td id=\"newsSubjectBackground\" style=\"font-size: large;\">Grade: </td><td> Not Allocated </td></tr>");
                    }
                    /*out.println("</table>");
                    session.setAttribute("numasgn", 1 + Integer.parseInt(session.getAttribute("numasgn") + ""));
                    out.println("<form  name=\"deadline\"  method=\"post\" onsubmit=\"return resetDeadline(" + assgn_ID + ")\">");
                    out.println("<table align=\"center\" width=\"60%\">");
                    out.println("<tr>");
                    out.println("<td>Deadline (dd-mm-yyyy)</td>");
                    out.println("<td><input name=\"SubmissionDate\" type=\"text\"  id=\"deadline_date\"></td>");
                    out.println("<td><input name=\"SubmissionTimeHrs\" type=\"number\" size=\"2\" id=\"deadline_time_hr\"></td>");
                    out.println("<td><input name=\"SubmissionTimeMin\" type=\"number\" size=\"2\" id=\"deadline_time_min\"></td>");
                    out.println("</tr>");
                    out.println("</table>");
                    out.println("<input type=\"submit\" value=\"Add\">");
                    out.println("</form>");*/
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                throw e;
            } finally {
                con.close();
                out.close();
            }
        } else {
            response.sendRedirect("index.jsp");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(getAsgn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(getAsgn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
