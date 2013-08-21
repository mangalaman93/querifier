/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
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
@WebServlet(name = "ServiceLoginAuth", urlPatterns = {"/ServiceLoginAuth"})
public class ServiceLoginAuth extends HttpServlet {

    private static final String SERVER = "localhost";
    private static final String DBNAME = "querifier";
    private static final String DB_USERNAME = "DB_project";
    private static final String DB_PASSWORD = "dbproject";
    private static final String instPage = "instructor.jsp";
    private static final String studentPage = "student.jsp";

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
            throws ServletException, IOException, SQLException, Exception {
        HttpSession session = request.getSession(false);
        String role = null, name = null;
        if (session != null) {
            if (!(session.getAttribute("loggedin") != null && session.getAttribute("loggedin").equals("true"))) {
                //getting the requsest parameter
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                //checking the username and password in database
                Connection con = null;
                boolean isValid = false;
                try {
                    con = connect();
                    String QUERY = "select * from user where ID = ? and passwd = ?";
                    PreparedStatement prepStmt = con.prepareStatement(QUERY);
                    prepStmt.setString(1, username);
                    prepStmt.setString(2, password);
                    ResultSet rs = prepStmt.executeQuery();
                    if (rs.next()) {
                        isValid = true;
                        role = rs.getString("role");
                        name = rs.getString("name");
                        QUERY = "select count(*) as num_assgn from assignment";
                        prepStmt = con.prepareStatement(QUERY);
                        rs = prepStmt.executeQuery();
                        if (rs.next()) {
                            session.setAttribute("numasgn", rs.getInt("num_assgn"));
                        }

                        //getting questions of the assignments
                        QUERY = "select assgn_ID, num_ques from assignment";
                        prepStmt = con.prepareStatement(QUERY);
                        rs = prepStmt.executeQuery();
                        while (rs.next()) {
                            session.setAttribute("asgn" + rs.getInt("assgn_ID") + "q", rs.getInt("num_ques"));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    throw e;
                } finally {
                    con.close();
                }

                //counting the number of attempts
                if (session.getAttribute("attemptNo") == null) {
                    session.setAttribute("attemptNo", 1);
                } else {
                    int attemptNo = Integer.parseInt("" + session.getAttribute("attemptNo"));
                    attemptNo++;
                    session.setAttribute("attemptNo", attemptNo);
                }
                //creating the session
                if (session != null && !session.isNew() && isValid) {
                    session.setAttribute("username", username);
                    session.setAttribute("name", name);
                    session.setAttribute("role", role);
                    session.setAttribute("loggedin", "true");
                } else {
                    session.setAttribute("loggedin", "invalid");
                }
            }

            if (session.getAttribute("loggedin") == null || session.getAttribute("loggedin").equals("invalid")) {
                response.sendRedirect("index.jsp");
            } else {
                role = "" + session.getAttribute("role");
                if ("instructor".equals(role)) {
                    response.sendRedirect(instPage);
                } else {
                    response.sendRedirect(studentPage);
                }
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
            Logger.getLogger(ServiceLoginAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ServiceLoginAuth.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServiceLoginAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ServiceLoginAuth.class.getName()).log(Level.SEVERE, null, ex);
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
