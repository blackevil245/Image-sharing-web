/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Image;

@WebServlet("/queryServlet")
public class ImageQuerying extends HttpServlet {
    
    EntityManager em;
    EntityManagerFactory emf;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            emf = Persistence.createEntityManagerFactory("Image_SharingPU");
            em = emf.createEntityManager();
            
            String queryOrder = request.getParameter("query_word");
            List<Image> images = null;
            
            if (queryOrder.equalsIgnoreCase("all")) {
                images = em.createNamedQuery("Image.findAll").getResultList();
            } else if (queryOrder.contains("user")) {
                String userID = queryOrder.substring(4);
                images = em.createNamedQuery("Image.findById").setParameter("ownerId", userID).getResultList();
            }
            
            Collections.reverse(images);
                    
            String json = new Gson().toJson(images);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.write(json);
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
        processRequest(request, response);
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
