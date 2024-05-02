package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.persistence.ConnectionPool;
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

        // Routing til frontpage

        app.get("/", ctx -> ctx.render("frontpage.html"));

        app.get("/personalData", ctx -> ctx.render("personalData.html"));
        app.get("/aboutUs", ctx -> ctx.render("aboutUs.html"));

        app.get("/termsAndConditions", ctx -> ctx.render("termsAndConditions.html"));
        app.get("/cancellationAndReturns", ctx -> ctx.render("cancellationAndReturns.html"));
        app.get("/warranty", ctx -> ctx.render("warranty.html"));
        app.get("/shipping", ctx -> ctx.render("shipping.html"));


    }
}