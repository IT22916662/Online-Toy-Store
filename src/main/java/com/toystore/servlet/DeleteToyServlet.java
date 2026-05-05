package com.toystore.servlet;

import com.toystore.dao.ToyDAO;
import com.toystore.util.AppInitializer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * DELETE operation. Removes the toy whose <code>id</code> query parameter
 * is supplied, then redirects back to the catalog with a status message.
 * The catalog page guards the action with a JavaScript confirm dialog.
 */
@WebServlet(name = "DeleteToyServlet", urlPatterns = {"/delete-toy"})
public class DeleteToyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ToyDAO dao = (ToyDAO) getServletContext().getAttribute(AppInitializer.DAO_KEY);

        String id = request.getParameter("id");
        boolean removed = id != null && !id.isBlank() && dao.delete(id);

        String msg = removed ? "deleted" : "notfound";
        response.sendRedirect(request.getContextPath() + "/toys?msg=" + msg);
    }
}
