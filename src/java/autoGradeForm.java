/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "autoGradeForm", urlPatterns = {"/autoGradeForm"})
public class autoGradeForm extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedin") != null && session.getAttribute("loggedin").equals("true")) {
            try {
                out.println("<form  name=\"autograde\"  method=\"post\" onsubmit=\"return autoGrade();\">");
                out.println("<table align=\"center\" width=\"60%\">");
                out.println("<tr>");
                out.println("<td>Assignment ID : </td><td><select name=\"assgn_id\" id=\"assgn_id\">");
                int numasgn = Integer.parseInt("" + session.getAttribute("numasgn"));
                for (int i = 1; i <= numasgn; i++) {
                    out.println("<option value=" + i + ">" + i + "</option>");
                }
                out.println("</select></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td>Cutoff for A : </td>");
                out.println("<td><input name=\"ct_A\" type=\"number\" size=\"10\" id=\"ct_A\"></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td>Cutoff for B</td>");
                out.println("<td><input name=\"ct_B\" type=\"number\" size=\"10\" id=\"ct_B\"></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td>Cutoff for C : </td>");
                out.println("<td><input name=\"ct_C\" type=\"number\" size=\"10\" id=\"ct_C\"></td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td>Cutoff for D : </td>");
                out.println("<td><input name=\"ct_D\" type=\"number\" size=\"10\" id=\"ct_D\"></td>");
                out.println("</tr></table>");
                out.println("<input type=\"submit\" value=\"grade\">");
                out.println("<input type=\"button\" value=\"Back\" onclick=\"getHome('"
                        + ("" + session.getAttribute("name")).toUpperCase() +"')\">");
                out.println("</form>");
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
        processRequest(request, response);
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
        processRequest(request, response);
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
