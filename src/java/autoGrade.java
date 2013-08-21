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
import java.sql.Statement;
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
 * @author amanmangal
 */
@WebServlet(name = "autoGrade", urlPatterns = {"/autoGrade"})
public class autoGrade extends HttpServlet {

    private static final String SERVER = "localhost";
    private static final String DBNAME = "querifier";
    private static final String DB_USERNAME = "DB_project";
    private static final String DB_PASSWORD = "dbproject";

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
            throws ServletException, IOException, SQLException, Exception {
        int coA = Integer.parseInt("" + request.getParameter("coA"));
        int coB = Integer.parseInt("" + request.getParameter("coB"));
        int coC = Integer.parseInt("" + request.getParameter("coC"));
        int coD = Integer.parseInt("" + request.getParameter("coD"));
        System.out.print("" + request.getParameter("asgn_ID") + coA + coB + coC + coD);
        int assgn_ID = Integer.parseInt("" + request.getParameter("asgn_ID"));
        int ID, marks = 1;
        float pcg;
        char grade = 'F';
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        if (session.getAttribute("loggedin") != null && session.getAttribute("loggedin").equals("true")) {
            Connection con = null;
            try {
                con = connect();
                String getID_QUERY = "select ID from user where role='student'";
                String insertGrade_QUERY = "INSERT INTO performance (ID, assgn_ID, grade) values(?, ?, ?)"
                        + " ON DUPLICATE KEY UPDATE grade = ?";
                String getMarks_QUERY = "select SUM(ques_marks) from submission as s, question"
                        + " as q where s.assgn_ID = ? and s.assgn_ID = q.assgn_ID and s.ques_no"
                        + " = q.ques_no and s.ID = ? and s.correct = 'YES'";
                String totalMarks_QUERY = "select SUM(ques_marks) from question where assgn_ID = ?";
                PreparedStatement prepStmt1 = con.prepareStatement(totalMarks_QUERY);
                prepStmt1.setInt(1, assgn_ID);
                ResultSet rs1 = prepStmt1.executeQuery();
                float total = 1;
                if (rs1.next()) {
                    total = rs1.getFloat(1);
                }
                Statement stmt = con.createStatement();
                rs1 = stmt.executeQuery(getID_QUERY);
                prepStmt1 = con.prepareStatement(getMarks_QUERY);
                PreparedStatement prepStmt2 = con.prepareStatement(insertGrade_QUERY);
                ResultSet rs2;
                while (rs1.next()) {
                    ID = rs1.getInt(1);
                    prepStmt1.setInt(1, assgn_ID);
                    prepStmt1.setInt(2, ID);
                    rs2 = prepStmt1.executeQuery();                    
                    if (rs2.next()) {
                        marks = rs2.getInt(1);
                    }
                    pcg = marks / total * 100;
                    if (pcg >= coA) {
                        grade = 'A';
                    } else if (pcg >= coB) {
                        grade = 'B';
                    } else if (pcg >= coC) {
                        grade = 'C';
                    } else if (pcg >= coD) {
                        grade = 'D';
                    }
                    prepStmt2.setString(3, "" +grade);
                    prepStmt2.setInt(2, assgn_ID);
                    prepStmt2.setInt(1, ID);
                    prepStmt2.setString(4, "" +grade);
                    prepStmt2.executeUpdate();
                }
                out.println("Grades set succesfully for all the students");
            } catch (Exception e) {
                out.println("Error: " + e.getMessage());
                throw e;
            } finally {
                con.close();
                out.close();
            }
        } else {
            response.sendRedirect("index.jsp");
        }
    }

    String getFeedback(String answer) {
        return "random";
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
        } catch (SQLException ex) {
            Logger.getLogger(autoGrade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(autoGrade.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (SQLException ex) {
            Logger.getLogger(autoGrade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(autoGrade.class.getName()).log(Level.SEVERE, null, ex);
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
