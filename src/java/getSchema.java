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
import java.sql.ResultSetMetaData;
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
@WebServlet(name = "getSchema", urlPatterns = {"/getSchema"})
public class getSchema extends HttpServlet {

    Connection connect(String SERVER, String DBNAME, String DB_USERNAME, String DB_PASSWORD) throws SQLException, Exception {
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
        String SERVER = "localhost";
        String DBNAME = "querifier";
        String DB_USERNAME = "DB_project";
        String DB_PASSWORD = "dbproject";
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedin") != null && session.getAttribute("loggedin").equals("true")) {
            //checking the username and password in database
            Connection con1 = null;
            Connection con2 = null;
            try {
                con1 = connect(SERVER, DBNAME, DB_USERNAME, DB_PASSWORD);
                int db_ID = Integer.parseInt("" + request.getParameter("db_ID"));
                String table = "" + request.getParameter("table");
                String QUERY = "select * from db where db_ID = ?";
                PreparedStatement prepStmt = con1.prepareStatement(QUERY);
                prepStmt.setInt(1, db_ID);
                ResultSet rs = prepStmt.executeQuery();
                if (rs.next()) {
                    SERVER = rs.getString("host");
                    DBNAME = rs.getString("db_name");
                    DB_USERNAME = rs.getString("username");
                    DB_PASSWORD = rs.getString("password");
                    con2 = connect(SERVER, DBNAME, DB_USERNAME, DB_PASSWORD);
                    QUERY = "describe " + table;
                    prepStmt = con2.prepareStatement(QUERY);
                    rs = prepStmt.executeQuery();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    out.print("<table border=\"1\" align=\"center\" width=\"30%\"><tr>");
                    for (int i = 1; i <= 6; i++) {
                        out.print("<td id=\"newsSubjectBackground\">" + rsmd.getColumnName(i));
                    }
                    while (rs.next()) {
                        out.print("<tr>");
                        for (int i = 1; i <= 6; i++) {
                            out.print("<td>" + rs.getString(i));
                        }
                    }
                    out.print("</table>");
                    out.println("<input type=\"button\" value=\"Back\" onclick=\"getTables(" + db_ID + ")\">");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                throw e;
            } finally {
                if (con1 != null) {
                    con1.close();
                }
                if (con2 != null) {
                    con2.close();
                }
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
            Logger.getLogger(getSchema.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(getSchema.class
                    .getName()).log(Level.SEVERE, null, ex);
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
