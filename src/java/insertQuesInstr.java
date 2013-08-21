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
 * @author amitpanghal
 */
@WebServlet(name = "insertQuesInstr", urlPatterns = {"/insertQuesInstr"})
public class insertQuesInstr extends HttpServlet {

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
            Connection con = null;
            //int num_ques = Integer.parseInt("" + session.getAttribute("Num_Ques"));
            //int assgn_ID = Integer.parseInt("" + session.getAttribute("Asgn_ID"));
            int i = 1;
            try {
                con = connect();
                String INSERT_QUERY, getDB_QUERY;
                INSERT_QUERY = "insert into assignment (assgn_ID, num_ques, deadline, mode, topic ) values (?,?,?,?,?)";
                getDB_QUERY = "select db_ID from db where db_name = ?";
                int assgn_ID = Integer.parseInt(request.getParameter("assgn_ID"));
                int num_ques = Integer.parseInt(request.getParameter("num_ques"));
                String rdate = "" + request.getParameter("date");
                int hour = Integer.parseInt(request.getParameter("hour"));
                int minute = Integer.parseInt(request.getParameter("minute"));
                String assgn_mode = request.getParameter("assgn_mode");
                String topic = request.getParameter("topic");
                String[] dateArray = rdate.split("-");
                int date = Integer.parseInt(dateArray[0]);
                int month = Integer.parseInt(dateArray[1]) - 1;
                int year = Integer.parseInt(dateArray[2]) - 1900;
                Timestamp deadline;
                int db_ID = 0;
                deadline = new Timestamp(year, month, date, hour, minute, 0, 0);

                PreparedStatement prepStmt = con.prepareStatement(INSERT_QUERY);
                prepStmt.setInt(1, assgn_ID);
                prepStmt.setInt(2, num_ques);
                prepStmt.setTimestamp(3, deadline);
                prepStmt.setString(4, assgn_mode);
                prepStmt.setString(5, topic);
                prepStmt.executeUpdate();
                //con = connect();
                String q_content, q_answer, q_database;
                int q_marks;
                INSERT_QUERY = "insert into question (assgn_ID, ques_no, content, answer, db_ID, ques_marks ) values (?,?,?,?,?,?)";
                while (i <= num_ques) {
                    q_content = request.getParameter("Question" + i);
                    q_answer = request.getParameter("Answer" + i);
                    q_marks = Integer.parseInt(request.getParameter("marks" + i));
                    q_database = request.getParameter("database" + i);
                    prepStmt = con.prepareStatement(getDB_QUERY);
                    prepStmt.setString(1, q_database);
                    System.out.println(q_database);
                    ResultSet rs = prepStmt.executeQuery();
                    if (rs.next()) {
                        db_ID = rs.getInt("db_ID");
                    }
                    prepStmt = con.prepareStatement(INSERT_QUERY);
                    prepStmt.setInt(1, assgn_ID);
                    prepStmt.setInt(2, i);
                    prepStmt.setString(3, q_content);
                    prepStmt.setString(4, q_answer);
                    prepStmt.setInt(5, db_ID);
                    prepStmt.setInt(6, q_marks);
                    prepStmt.executeUpdate();
                    i++;
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
            Logger.getLogger(insertQuesInstr.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(insertQuesInstr.class.getName()).log(Level.SEVERE, null, ex);
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
