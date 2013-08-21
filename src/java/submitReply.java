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
 * @author amitpanghal
 */
@WebServlet(name = "submitReply", urlPatterns = {"/submitReply"})
public class submitReply extends HttpServlet {

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
        int news_ID = Integer.parseInt("" + request.getParameter("news_ID"));
        if (session != null && session.getAttribute("loggedin") != null && session.getAttribute("loggedin").equals("true")) {
            Connection con = null;
            int reply_ID = 1;
            boolean isValid = false;
            try {
                con = connect();
                String COUNT_QUERY = "select reply_ID from news where news_ID = " + news_ID + " order by reply_ID DESC";
                String SUBJECT_QUERY = "select sub from news where news_ID = " + news_ID + " and reply_ID = 0";
                String INSERT_QUERY, subject, content;
                subject = "RE: ";
                PreparedStatement pStmt1, pStmt2;
                pStmt1 = con.prepareStatement(COUNT_QUERY);
                ResultSet rs1;
                rs1 = pStmt1.executeQuery(COUNT_QUERY);
                pStmt2 = con.prepareStatement(SUBJECT_QUERY);               
                ResultSet rs2;
                rs2 = pStmt2.executeQuery(SUBJECT_QUERY);
                if (rs1.next() && rs2.next()) {
                    isValid = true;
                }
                if (isValid) {
                    reply_ID += rs1.getInt(1);
                    subject += rs2.getString("sub");
                }

                content = "" + request.getParameter("Content");
                System.out.println(content);
                int ID = Integer.parseInt("" + session.getAttribute("username"));
                INSERT_QUERY = "insert into news (news_ID,reply_ID, ID, sub, content ) values (?,?,?,?,?)";
                PreparedStatement prepStmt = con.prepareStatement(INSERT_QUERY);
                prepStmt.setInt(1, news_ID);
                prepStmt.setInt(2, reply_ID);
                prepStmt.setInt(3, ID);
                prepStmt.setString(4, subject);
                prepStmt.setString(5, content);
                prepStmt.executeUpdate();
                out.print("successfully added \"" + subject + "\'");
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
            Logger.getLogger(submitReply.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(submitReply.class.getName()).log(Level.SEVERE, null, ex);
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
