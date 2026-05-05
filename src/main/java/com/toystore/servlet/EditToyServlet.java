package com.toystore.servlet;

import com.toystore.dao.ToyDAO;
import com.toystore.model.Toy;
import com.toystore.model.ToyFactory;
import com.toystore.util.AppInitializer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * UPDATE operation. GET loads the existing toy by ID and shows the edit
 * form pre-populated with current values. POST validates the new values,
 * rebuilds the matching subclass via {@link ToyFactory}, and asks the DAO
 * to overwrite the existing record.
 */
@WebServlet(name = "EditToyServlet", urlPatterns = {"/edit-toy"})
public class EditToyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ToyDAO dao = (ToyDAO) getServletContext().getAttribute(AppInitializer.DAO_KEY);
        String id = request.getParameter("id");

        Optional<Toy> existing = dao.findById(id == null ? "" : id);
        if (existing.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/toys");
            return;
        }

        request.setAttribute("toy", existing.get());
        request.getRequestDispatcher("/WEB-INF/views/edit-toy.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ToyDAO dao = (ToyDAO) getServletContext().getAttribute(AppInitializer.DAO_KEY);

        String id = trim(request.getParameter("id"));
        String type = trim(request.getParameter("type"));
        String name = trim(request.getParameter("name"));
        String priceStr = trim(request.getParameter("price"));
        String stockStr = trim(request.getParameter("stock"));
        String ageGroup = trim(request.getParameter("ageGroup"));
        String extra = trim(request.getParameter("extra"));

        try {
            if (id.isEmpty() || type.isEmpty() || name.isEmpty()
                    || priceStr.isEmpty() || stockStr.isEmpty() || ageGroup.isEmpty()) {
                throw new IllegalArgumentException("All fields are required");
            }
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);

            Toy updated = ToyFactory.create(type, id, name, price, stock, ageGroup, extra);
            boolean saved = dao.update(updated);

            if (!saved) {
                throw new IllegalArgumentException("Toy not found: " + id);
            }
            response.sendRedirect(request.getContextPath() + "/toys?msg=updated");
        } catch (NumberFormatException e) {
            forwardWithError(request, response, "Price and stock must be numeric.");
        } catch (IllegalArgumentException e) {
            forwardWithError(request, response, e.getMessage());
        }
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response,
                                  String message) throws ServletException, IOException {
        ToyDAO dao = (ToyDAO) getServletContext().getAttribute(AppInitializer.DAO_KEY);
        Optional<Toy> existing = dao.findById(request.getParameter("id"));

        request.setAttribute("error", message);
        // Rebuild a dummy Toy from the submitted (possibly invalid) values so the
        // form re-displays exactly what the user typed instead of the saved values.
        request.setAttribute("toy", existing.orElse(null));
        request.setAttribute("formName", request.getParameter("name"));
        request.setAttribute("formPrice", request.getParameter("price"));
        request.setAttribute("formStock", request.getParameter("stock"));
        request.setAttribute("formAgeGroup", request.getParameter("ageGroup"));
        request.setAttribute("formExtra", request.getParameter("extra"));

        request.getRequestDispatcher("/WEB-INF/views/edit-toy.jsp").forward(request, response);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
