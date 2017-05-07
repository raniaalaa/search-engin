/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mennna
 */
@WebServlet(name = "Engine", urlPatterns = {"/Engine"})
public class Engine extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, Exception {
      
        
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("text/html;charset=UTF-8");
          out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><link rel=\"stylesheet\" type=\"text/css\" href=\"css/resCss.css\">\n");
            out.println("<title>Servlet Engine</title>");            
            out.println("</head>");
            String query;
            query = request.getParameter("query");
            //String s=RANKER.temp(query);
            DATABASE d=new DATABASE();
            int w=d.GetImportance(query,38);
            Long[] Docs=RANKER.Ranking(query);
			for(int i=0;i<Docs.length;i++)
			{
			    System.out.println(d.getDocTitle(Docs[i]));	
                            System.out.println(Docs[i]);
                            System.out.println(d.getUrl(Docs[i]));
			}
                        for (int i =0; i<Docs.length;++i){
                    
               out.println(
                "<li style=\"list-style-type:none\">\n"
                + "<div>\n" +
                "<p color=\"blue\">"+d.getDocTitle(Docs[i])+"<br>\n" +
                "<a href=\""+d.getUrl(Docs[i])+"\" style=\"color:\"blue\">" +
                        //results.get(i).getUrl()+"</a><br>\n" +
               // "<span style=\" color:#808080\">"+results.get(i).getDescp()+"</p>\n" +
                "</div></li>\n"); 
                        }
            /* TODO output your page here. You may use following sample code. */
            
            out.println("<body>");
            out.println("</body>");
            out.println("</html>");
           
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
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
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
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
