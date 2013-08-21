/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
 * @author amitpanghal
 */
@WebServlet(name = "insertAsgnInstr", urlPatterns = {"/insertAsgnInstr"})
public class insertAsgnInstr extends HttpServlet {

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
            Connection con = connect();
            String QUERY = "select db_name from db";
            PreparedStatement pStmt;
            pStmt = con.prepareStatement(QUERY);
            ResultSet rs = pStmt.executeQuery(QUERY);
            
            String topic, assgn_mode;
            topic = request.getParameter("topic");
            assgn_mode = request.getParameter("mode");
            int hour = 0, minute = 0, num_ques = 0, i = 1;
            num_ques = Integer.parseInt(request.getParameter("numQ"));
            String rDate = request.getParameter("date");
            hour = Integer.parseInt(request.getParameter("hr"));
            minute = Integer.parseInt(request.getParameter("min"));

            int assgn_ID;
            assgn_ID = Integer.parseInt("" + request.getParameter("assgn_ID"));
            try {
                out.println("<form  name=\"Add\"  method=\"post\"  onsubmit=\"return insertQuesI(" + assgn_ID + "," + num_ques + ",'" + rDate + "'," + hour + "," + minute + ",'" + assgn_mode + "','" + topic + "');\">");
                out.println("<table align=\"center\" width=\"60%\">");

                while (i <= num_ques) {
                    out.println("<tr>");
                    out.println("<td>Question:" + i + "</td>");
                    out.println("<td><textarea name=\"Question" + i + "\" cols=\"30\" rows=\"3\"></textarea></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td>Answer:" + "</td>");
                    out.println("<td><textarea name=\"Answer" + i + "\" cols=\"30\" rows=\"3\"></textarea></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td>Marks:" + "</td>");
                    out.println("<td><input name=\"marks" + i + "\" type=\"number\" size=\"5\"/></td>");
                    out.println("</tr>");
                    out.println("<td> Database : </td><td><select name=\"database" + i + "\">");
                    while (rs.next()) {
                        String name = rs.getString("db_name");
                        out.println("<option value=" + name + ">" + name + "</option>");
                    }
                    out.println("</select></td>");
                    out.println("</tr>");
                    i++;
                }
                out.println("</table>");
                out.println("<input type=\"submit\" value=\"Add\">");
                out.println("<input type=\"button\" value=\"Back\" onclick=\"addAsgnFormInstr()\">");
                out.println("</form>");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                throw e;
            } finally {
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
            Logger.getLogger(insertAsgnInstr.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(insertAsgnInstr.class.getName()).log(Level.SEVERE, null, ex);
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
