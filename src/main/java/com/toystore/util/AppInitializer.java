package com.toystore.util;

import com.toystore.dao.ToyDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Application bootstrap. Creates a single {@link ToyDAO} when the webapp
 * starts and stores it in the {@link ServletContext} so every servlet
 * shares the same instance.
 */
@WebListener
public class AppInitializer implements ServletContextListener {

    public static final String DAO_KEY = "toyDao";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext ctx = event.getServletContext();
        FileHandler fileHandler = FileHandler.forContext(ctx, "toys.txt");
        ToyDAO dao = new ToyDAO(fileHandler);
        ctx.setAttribute(DAO_KEY, dao);
    }
}
