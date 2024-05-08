package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController {

    public static void addRoutes(Javalin app, ConnectionPool connectionpool) {

        app.get("/", ctx -> frontpage(ctx, connectionpool));

        app.get("/login", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> login(ctx, connectionpool));

        app.get("/personalData", ctx -> ctx.render("personalData.html"));
        app.get("/aboutUs", ctx -> ctx.render("aboutUs.html"));
        app.get("/termsAndConditions", ctx -> ctx.render("termsAndConditions.html"));
        app.get("/cancellationAndReturns", ctx -> ctx.render("cancellationAndReturns.html"));
        app.get("/warranty", ctx -> ctx.render("warranty.html"));
        app.get("/shipping", ctx -> ctx.render("shipping.html"));

        app.get("/orderStatus", ctx -> ctx.render("orderStatus.html"));
        app.get("/designCarport", ctx -> ctx.render("designCarport.html"));



    }

    private static void frontpage(Context ctx, ConnectionPool connectionpool) {
        ctx.render("frontpage.html");
    }

    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate(); //sletter alle data,
        ctx.render("frontpage");
    }

    public static void login(Context ctx, ConnectionPool connectionPool) {
        //metoden henter http-anmodning
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            //Der forsøges at logget ind
            User user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser",user); //brugeren bliver gemt

            ctx.render("frontpage.html");

        } catch (DatabaseException e) {

            ctx.attribute("message", e.getMessage());


            ctx.render("login.html");
        }
    }
}