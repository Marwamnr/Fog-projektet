package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;

import app.controllers.UserController;

import app.controllers.OrderController;
import app.controllers.OrderStatusController;

import app.persistence.ConnectionPool;
import app.persistence.OrderStatusMapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "fog";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {


        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);



        // Add routes for controllers
        UserController.addRoutes(app, connectionPool);
        OrderController.addRoutes(app, connectionPool);
        OrderStatusController.addRoutes(app, connectionPool);




    }
}