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

/**
 * CREATE operation. GET shows the empty add form; POST validates the form,
 * builds the correct {@link Toy} subclass via {@link ToyFactory}, and asks
 * the DAO to persist it.
 */
@WebServlet(name = "AddToyServlet", urlPatterns = {"/add-toy"})
public class AddToyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ToyDAO dao = (ToyDAO) getServletContext().getAttribute(AppInitializer.DAO_KEY);
        request.setAttribute("suggestedId", dao.nextId());
        request.getRequestDispatcher("/WEB-INF/views/add-toy.jsp").forward(request, response);
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

            Toy toy = ToyFactory.create(type, id, name, price, stock, ageGroup, extra);
            dao.add(toy);

            response.sendRedirect(request.getContextPath() + "/toys?msg=added");
        } catch (NumberFormatException e) {
            forwardWithError(request, response, "Price and stock must be numeric.");
        } catch (IllegalArgumentException e) {
            forwardWithError(request, response, e.getMessage());
        }
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response,
                                  String message) throws ServletException, IOException {
        request.setAttribute("error", message);
        // Echo submitted values back to the form so the user does not retype them.
        request.setAttribute("formId", request.getParameter("id"));
        request.setAttribute("formType", request.getParameter("type"));
        request.setAttribute("formName", request.getParameter("name"));
        request.setAttribute("formPrice", request.getParameter("price"));
        request.setAttribute("formStock", request.getParameter("stock"));
        request.setAttribute("formAgeGroup", request.getParameter("ageGroup"));
        request.setAttribute("formExtra", request.getParameter("extra"));
        request.getRequestDispatcher("/WEB-INF/views/add-toy.jsp").forward(request, response);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
