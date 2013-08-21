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
@WebServlet(name = "acceptAnswer", urlPatterns = {"/acceptAnswer"})
public class acceptAnswer extends HttpServlet {

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

    String getFeedback(String SERVER, String DBNAME, String DB_USERNAME, String DB_PASSWORD, String ideal, String answer) throws Exception {
        Connection con = null;
        ideal = ideal.trim();
        answer = answer.trim();
        String feedback = "";
        String correct = "";
        boolean format, rsequal;
        int r;
        if (ideal.equals(answer)) {
            feedback = "Flawless query! Excellent!!";
            correct = "YES";
        } else {
            try {
                con = connect(SERVER, DBNAME, DB_USERNAME, DB_PASSWORD);
                String union_Query = answer + " union " + ideal;
                //String suff_Query = ideal + " minus (" + answer + ")";
                Statement stmt1 = con.createStatement();
                Statement stmt2 = con.createStatement();
                Statement stmt = con.createStatement();
                ResultSet rs1 = stmt1.executeQuery(answer);
                ResultSet rs2 = stmt2.executeQuery(ideal);
                System.out.print("ragwa");
                ResultSetMetaData rsmd2 = rs2.getMetaData();
                System.out.print("ragwa");
                ResultSetMetaData rsmd1 = rs1.getMetaData();
                System.out.print("ragwa");
                int c1 = rsmd1.getColumnCount();
                int c2 = rsmd2.getColumnCount();
                if (c1 != c2) {
                    feedback = "incorrect no. of columns";
                    correct = "NO";
                } else {
                    format = true;
                    for (int i = 1; i <= c1; i++) {
                        if (!rsmd1.getColumnName(i).equals(rsmd2.getColumnName(i))) {
                            format = false;
                            feedback = "incorrect field order";
                            correct = "NO";
                            break;
                        }
                    }
                    if (format) {
                        ResultSet rs = stmt.executeQuery(union_Query);
                        ResultSetMetaData rsmd = rs.getMetaData();
                        r = 0;
                        while (rs.next()) {
                            r++;
                        }
                        while (rs2.next()) {
                            r--;
                        }
                        if (r > 0) {
                            feedback += " Not all produced rows are required.";
                            correct = "NO";
                            if (((ideal.indexOf("DISTINCT") > -1) || (ideal.indexOf("distinct") > -1)) && (answer.indexOf("DISTINCT") == -1) && (answer.indexOf("distinct") == -1)) {
                                feedback += " Use of 'distinct' probably required.";
                            }
                        }
                        r = 0;
                        rs.beforeFirst();
                        while (rs.next()) {
                            r++;
                        }
                        while (rs1.next()) {
                            r--;
                        }
                        if (r > 0) {
                            feedback += " Not all required rows are produced.";
                            correct = "NO";
                        }
                        if (!correct.equals("NO")) {
                            rsequal = true;
                            rs1.beforeFirst();
                            rs2.beforeFirst();
                            while (rs1.next() && rs2.next()) {
                                for (int i = 1; i <= c1; i++) {
                                    if (!(rs1.getObject(i).equals(rs2.getObject(i)))) {
                                        rsequal = false;
                                        break;
                                    }
                                }
                                if (!rsequal) {
                                    break;
                                }
                            }
                            if (rsequal) {
                                feedback += "Correct result produced. Optimization may be possible.";
                                correct = "YES";
                            } else {
                                feedback += " Output of the query not ordered as required.";
                                correct = "NO";
                                if (((ideal.indexOf("order by") > -1) || (ideal.indexOf("ORDER BY") > -1))
                                        && (((answer.indexOf("unique") != -1)
                                        && (answer.indexOf("unique") != -1))
                                        || (((ideal.indexOf("desc") > -1)
                                        || (ideal.indexOf("DESC") > -1))
                                        && (answer.indexOf("unique") != -1)
                                        && (answer.indexOf("unique") != -1)))) {
                                    feedback += " 'order by' or 'desc' clauses may be required.";
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.print("Error: " + e.getMessage());
                feedback += "sql syntax error ";
                correct = "NO";
                //throw e;
            } finally {
                con.close();
            }
        }
        feedback = correct + feedback;
        return feedback;
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
        response.setContentType("text/html;charset=UTF-8");

        String SERVER = "localhost";
        String DBNAME = "querifier";
        String DB_USERNAME = "DB_project";
        String DB_PASSWORD = "dbproject";
        int assgn_ID = Integer.parseInt("" + request.getParameter("asgn_ID"));
        int ques_no = Integer.parseInt("" + request.getParameter("ques_no"));
        String ans = request.getParameter("answer").toString();
        String feedback = "";
        String correct = "YES";
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);

        if (session.getAttribute("loggedin") != null && session.getAttribute("loggedin").equals("true")) {
            int ID = Integer.parseInt("" + session.getAttribute("username"));
            Connection con = null;
            try {
                con = connect(SERVER, DBNAME, DB_USERNAME, DB_PASSWORD);
                String getDB_QUERY = "select db.host,db.username,db.password,db.db_name,q.answer from db,question as q where q.db_ID=db.db_ID and q.assgn_ID = ? and q.ques_no = ?";
                String Edit_QUERY = "update submission set ans_query = ? , feedback = ? , correct = ? where assgn_ID = ? and ques_no =? and ID = ?";
                String Add_QUERY = "insert into submission (ID,assgn_ID,ques_no,ans_query,feedback,correct) values(?, ?, ?, ?, ?, ?)";
                String Sub_QUERY = "select * from submission where assgn_ID = ? and ques_no = ? and ID = ?";
                PreparedStatement prepStmt = con.prepareStatement(getDB_QUERY);
                prepStmt.setInt(1, assgn_ID);
                prepStmt.setInt(2, ques_no);
                ResultSet rs = prepStmt.executeQuery();
                if (rs.next()) {
                    feedback = getFeedback(rs.getString("host"), rs.getString("db_name"), rs.getString("username"),
                            rs.getString("password"), rs.getString("answer"), ans);
                    if (feedback.startsWith("YES")) {
                        correct = "YES";
                        feedback = feedback.substring(3);
                    } else if (feedback.startsWith("NO")) {
                        correct = "NO";
                        feedback = feedback.substring(2);
                    }
                }

                prepStmt = con.prepareStatement(Sub_QUERY);
                prepStmt.setInt(1, assgn_ID);
                prepStmt.setInt(2, ques_no);
                prepStmt.setInt(3, ID);
                rs = prepStmt.executeQuery();
                if (rs.next()) {
                    prepStmt = con.prepareStatement(Edit_QUERY);
                    prepStmt.setString(1, ans);
                    prepStmt.setString(2, feedback);
                    prepStmt.setString(3, correct);
                    prepStmt.setInt(4, assgn_ID);
                    prepStmt.setInt(5, ques_no);
                    prepStmt.setInt(6, ID);
                } else {
                    prepStmt = con.prepareStatement(Add_QUERY);
                    prepStmt.setString(4, ans);
                    prepStmt.setString(5, feedback);
                    prepStmt.setString(6, correct);
                    prepStmt.setInt(2, assgn_ID);
                    prepStmt.setInt(3, ques_no);
                    prepStmt.setInt(1, ID);
                }
                prepStmt.executeUpdate();
                out.println("Submission Succesful for question " + ques_no + " of assignment " + assgn_ID);

            } catch (Exception e) {
                out.print("Error: " + e.getMessage());
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
            Logger.getLogger(acceptAnswer.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(acceptAnswer.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(acceptAnswer.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(acceptAnswer.class
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
