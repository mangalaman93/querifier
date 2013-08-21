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
 * @author amanmangal
 */
@WebServlet(name = "setGrade", urlPatterns = {"/setGrade"})
public class setGrade extends HttpServlet {

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
        //HttpSession session = request.getSession(false);
        int assgn_ID = Integer.parseInt("" + request.getParameter("asgn_ID"));
        String grade = request.getParameter("grade");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        if (session.getAttribute("loggedin") != null && session.getAttribute("loggedin").equals("true")) {
            int ID = Integer.parseInt("" + request.getParameter("ID"));
            Connection con = null;
            try {
                con = connect();
                String Edit_QUERY = "update performance set grade = ? where ID = ? and assgn_ID = ?";
                String Add_QUERY = "insert into performance (ID,assgn_ID,grade) values(?, ?, ?)";
                String grad_QUERY = "select * from performance where assgn_ID = ? and ID = ?";
                PreparedStatement prepStmt = con.prepareStatement(grad_QUERY);
                prepStmt.setInt(1, assgn_ID);
                prepStmt.setInt(2, ID);
                ResultSet rs = prepStmt.executeQuery();
                if (rs.next()) {
                    prepStmt = con.prepareStatement(Edit_QUERY);
                    prepStmt.setString(1, "" + grade);
                    prepStmt.setInt(3, assgn_ID);
                    prepStmt.setInt(2, ID);
                } else {
                    prepStmt = con.prepareStatement(Add_QUERY);
                    prepStmt.setString(3, "" + grade);
                    prepStmt.setInt(2, assgn_ID);
                    prepStmt.setInt(1, ID);
                }
                prepStmt.executeUpdate();
                out.println("Grade succesfully updated for student " + ID + " for assignment " + assgn_ID);
                //out.println("<form action=\"getQuestion\" name=\"GoBack\" method=\"post\" onsubmit=\"return validateSubmission()\"">
                //out.println("<%Submitted(" +assgn_ID + "," + ques_no + ")%>");
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
        } catch (SQLException ex) {
            Logger.getLogger(setGrade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(setGrade.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(setGrade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(setGrade.class.getName()).log(Level.SEVERE, null, ex);
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
