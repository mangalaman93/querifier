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
import java.sql.Timestamp;
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
@WebServlet(name = "getQuestion", urlPatterns = {"/getQuestion"})
public class getQuestion extends HttpServlet {

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
        int assgn_ID = Integer.parseInt("" + request.getParameter("asgn_ID"));
        int ques_no = Integer.parseInt("" + request.getParameter("ques_no"));
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        if (session.getAttribute("loggedin") != null && session.getAttribute("loggedin").equals("true")) {
            int ID = Integer.parseInt("" + session.getAttribute("username"));
            Connection con = null;
            boolean isValid = false;
            try {
                con = connect();
                String Ques_QUERY = "select * from question natural join db where assgn_ID = ? and ques_no = ?";
                String Sub_QUERY = "select * from submission where assgn_ID = ? and ques_no = ? and ID = ?";
                String mode_QUERY = "select mode,deadline from assignment where assgn_ID =?";
                String getTS_QUERY = "select CURRENT_TIMESTAMP";
                PreparedStatement prepStmt1 = con.prepareStatement(Ques_QUERY);
                prepStmt1.setInt(1, assgn_ID);
                prepStmt1.setInt(2, ques_no);
                ResultSet rs1 = prepStmt1.executeQuery();
                PreparedStatement prepStmt2 = con.prepareStatement(Sub_QUERY);
                prepStmt2.setInt(1, assgn_ID);
                prepStmt2.setInt(2, ques_no);
                prepStmt2.setInt(3, ID);
                ResultSet rs2 = prepStmt2.executeQuery();
                if (rs1.next()) {
                    isValid = true;
                }
                if (isValid) {
                    String content = rs1.getString("content");
                    String database = rs1.getString("db_ID");
                    String ques_marks = ("" + rs1.getInt("ques_marks"));
                    String db = rs1.getString("db_name");
                    String prev_ans = "";
                    String feedback = "";
                    if (rs2.next()) {
                        prev_ans = rs2.getString("ans_query");
                        feedback = rs2.getString("feedback");
                    }
                    prepStmt1 = con.prepareStatement(getTS_QUERY);
                    rs1 = prepStmt1.executeQuery();
                    Timestamp t = null, deadline = null;
                    String mode = "";
                    if (rs1.next()) {
                        t = rs1.getTimestamp(1);
                    }
                    prepStmt2 = con.prepareStatement(mode_QUERY);
                    prepStmt2.setInt(1, assgn_ID);
                    rs2 = prepStmt2.executeQuery();
                    if (rs2.next()) {
                        mode = rs2.getString(1);
                        deadline = rs2.getTimestamp(2);
                    }
                    // String deadline = ("" + rs.getDate("deadline"));
                    // String post_time = ("" + rs.getTimestamp("post_time"));
                    out.println("<h1>Assignment: " + assgn_ID + "</h1>");
                    out.println("<p><span style=\"font-size: 23px; text-align: left !important;\">Question " + ques_no + " :</span>");
                    out.println(content + "<b>[on database: " + db + "]</b></p><br>");
                    String role = "" + session.getAttribute("role");
                    if (role.equalsIgnoreCase("student")) {
                        if (prev_ans.equals("")) {
                            out.println("<p><b>No Previous submission</b></p>");
                        } else {
                            out.println("<p><b>Previous submission</b>: " + prev_ans + "</p>");
                            if (t.compareTo(deadline) > 0 || mode.equals("learning")) {
                                out.println("<p><b>Feedback</b>: " + feedback + "</p>");
                            }
                        }
                        if (t.compareTo(deadline) < 0) {
                            out.println("<form name=\"AnswerSubmission\" method=\"post\" "
                                    + "onsubmit=\"return validateSubmission(" + assgn_ID + "," + ques_no + ")"
                                    + "\"><table><tr><td>Add/Edit Submission:<br>(Do not add ';' at the end)<td><textarea name=\"answer\" cols=\"70\" rows=\"5\"></textarea>" 
                                    + "</table><input type=\"submit\" value=\"Submit\"></form>");
                        }
                        //out.println("<h3>Deadline: "+deadline+"</h3>");
                        //out.println("<h3>Posted On: "+post_time+"</h3>");
                    }
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
            Logger.getLogger(getQuestion.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(getQuestion.class.getName()).log(Level.SEVERE, null, ex);
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
