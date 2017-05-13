/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
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
     //boolean phrase;
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
            out.println("<head>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/newcss.css\">\n");
            String query;
            query = request.getParameter("query");
            out.println("<title>"+query+" - Search</title>");            
            out.println("</head>");
            out.println("<body >");
            out.println("<div id=\"res\" > "); 
            out.println("<a href=\"http://localhost:8080/Search/\"> "); 
            out.println("<img src=\"images/transparent.png\" width=\"150\" height=\"90\" alt=\"search\">");
            out.println("</a>");
            out.println("<form action=\"Engine\"  method=\"POST\" >"); 
            out.println("<input  id=\"restextbox\" type=\"text\" name=\"query\"  placeholder=\"Search here...\" /> </div>");
            query=query.toLowerCase();
            System.out.println(query);
            
            FILER f=new FILER();
            DATABASE d=new DATABASE();
            PARSER p=new PARSER();
            String [] Words= p.Parse(query);
            ArrayList<Long> docsList=p.GetIntersection();
            Long[] Docs = new Long[docsList.size()];
            Docs = docsList.toArray(Docs);
            Long[] Sorteddocs=RANKER.Ranking(Docs,Words);
                        
                        for (int i =0; i<Docs.length;++i)
                        {
                            out.println( "<li style=\"list-style-type:none\">\n"
                            + "<div style=\"margin: 1cm 15cm 0.5cm 1.5cm\" >\n" 
                                    +
                             "<a href=\""+d.getUrl(Docs[i])+"\" style=\"color:\"blue\">" +
                               d.getDocTitle(Docs[i])+"</a><br>\n" + 
                             "<span style=\" color:#CAD3D3\">"+f.getDescription(query,Docs[i])+"\n" +
                             "</div></li>\n");    
                        //style=\" color:#808080\"
                        //BFD3D3  
                        }
                        docsList=p.GetDiff();
                        if(Sorteddocs.length==0&&docsList.size()==0)
                        {
                            out.println("<p style=\"margin: 2cm 4cm 3cm 10cm; color:#CADCDC; font-size: 40px;\"> <b>Sorry, Your query is not found</b></p>"); /////azabt l html bta3ha
                         
                        }
                        Docs = new Long[docsList.size()];
                        Docs = docsList.toArray(Docs);
                        Sorteddocs=RANKER.Ranking(Docs,Words);
                        for (int i =0; i<Sorteddocs.length;++i)
                        {
                            //3ashan a3ml l kalam elly fel query bold////////////
                            ///////////*use <strong> or <b> tag also, you can try with css <span style="font-weight:bold">text</span> *///////////

                            out.println( "<li style=\"list-style-type:none\">\n"
                            + "<div style=\"margin: 1cm 15cm 0.5cm 1.5cm\" >\n" 
                                    +
                             "<a href=\""+d.getUrl(Docs[i])+"\" style=\"color:\"blue\">" +
                               d.getDocTitle(Docs[i])+"</a><br>\n" +
                                      // ++
                             "<span style=\" color:#CAD3D3\">"+f.getDescription(query,Docs[i])+"\n" +
                             "</div></li>\n");          
                        // style=\" color:#808080\"
                        }
                        
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