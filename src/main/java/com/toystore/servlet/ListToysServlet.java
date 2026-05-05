package com.toystore.servlet;

import com.toystore.dao.ToyDAO;
import com.toystore.model.Toy;
import com.toystore.util.AppInitializer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * READ operation. Displays every toy in the catalog and supports filtering
 * by name fragment and/or category. The same page also offers entry points
 * for the Update and Delete operations.
 */
@WebServlet(name = "ListToysServlet", urlPatterns = {"/toys"})
public class ListToysServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ToyDAO dao = (ToyDAO) getServletContext().getAttribute(AppInitializer.DAO_KEY);

        String nameQuery = request.getParameter("name");
        String category = request.getParameter("category");

        List<Toy> toys = dao.search(nameQuery, category);

        request.setAttribute("toys", toys);
        request.setAttribute("nameQuery", nameQuery == null ? "" : nameQuery);
        request.setAttribute("category", category == null ? "" : category);
        request.setAttribute("msg", request.getParameter("msg"));

        request.getRequestDispatcher("/WEB-INF/views/toy-list.jsp").forward(request, response);
    }
}
