/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
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
 * @author akshay
 */
@WebServlet(name = "update", urlPatterns = {"/update"})
public class update extends HttpServlet {

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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedin") != null && session.getAttribute("loggedin").equals("true")) {
            Connection con = null;
            try {
                con = connect();
                int user_ID = Integer.parseInt("" + session.getAttribute("username"));
                PreparedStatement prepStmt;
                ResultSet rs;
                String DEADLINE_QUERY = "select * from assignment as a where (a.deadline-NOW())>0 order by deadline limit 5";
                String NEWS_QUERY = "select * from news order by time desc limit 5";
                if (session.getAttribute("role").equals("student")) {
                    String GRADES_QUERY = "select * from assignment as a natural join performance as  p where ID=? order by p.time desc limit 5";
                    prepStmt = con.prepareStatement(GRADES_QUERY);
                    prepStmt.setInt(1, user_ID);
                    rs = prepStmt.executeQuery();
                    out.println("<p id=\"smallHeading\">Graded assignments<br>");
                    while (rs.next()) {
                        String name = rs.getString("topic");
                        int id = rs.getInt("assgn_ID");
                        // String deadline = ("" + rs.getDate("deadline"));
                        // String post_time = ("" + rs.getTimestamp("post_time"));
                        out.println("<a id=\"updates\" href=\"javascript:getAsgn(" + id + ");\">" + "Assignment" + id + "</a></br>");
                        //out.println("<h3>Deadline: "+deadline+"</h3>");
                        //out.println("<h3>Posted On: "+post_time+"</h3>");
                    }
                    out.print("</br>");
                }
                prepStmt = con.prepareStatement(DEADLINE_QUERY);
                rs = prepStmt.executeQuery();
                out.println("Upcoming deadlines<br>");
                while (rs.next()) {
                    String name = rs.getString("topic");
                    int id = rs.getInt("assgn_ID");
                    // String deadline = ("" + rs.getDate("deadline"));
                    // String post_time = ("" + rs.getTimestamp("post_time"));
                    out.println("<a id=\"updates\" href=\"javascript:getAsgn(" + id + ");\">" + "Assignment" + id + "</a></br>");
                    //out.println("<h3>Deadline: "+deadline+"</h3>");
                    //out.println("<h3>Posted On: "+post_time+"</h3>");
                }
                out.print("</br>");
                prepStmt = con.prepareStatement(NEWS_QUERY);
                rs = prepStmt.executeQuery();
                out.println("Latest news<br>");
                while (rs.next()) {
                    String name = rs.getString("sub");
                    int id = rs.getInt("news_ID");
                    // String deadline = ("" + rs.getDate("deadline"));
                    // String post_time = ("" + rs.getTimestamp("post_time"));
                    out.println("<a id=\"updates\" href=\"javascript:getNews(" + id + ");\">" + name + "</a></br>");
                    //out.println("<h3>Deadline: "+deadline+"</h3>");
                    //out.println("<h3>Posted On: "+post_time+"</h3>");
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
            Logger.getLogger(update.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(update.class.getName()).log(Level.SEVERE, null, ex);
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
