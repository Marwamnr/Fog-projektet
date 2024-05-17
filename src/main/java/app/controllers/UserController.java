package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController {

    public static void addRoutes(Javalin app, ConnectionPool connectionpool) {

        app.get("/login", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> login(ctx, connectionpool));

        app.post("/logout", ctx -> logout(ctx));


        app.get("/personalData", ctx -> ctx.render("personalData.html"));
        app.get("/aboutUs", ctx -> ctx.render("aboutUs.html"));
        app.get("/termsAndConditions", ctx -> ctx.render("termsAndConditions.html"));
        app.get("/cancellationAndReturns", ctx -> ctx.render("cancellationAndReturns.html"));
        app.get("/warranty", ctx -> ctx.render("warranty.html"));
        app.get("/shipping", ctx -> ctx.render("shipping.html"));


        app.get("/designCarport", ctx -> ctx.render("designCarport.html"));


        app.post("/createAccount", ctx -> ctx.render("createUser.html"));
        app.post("/submit", ctx -> createUser(ctx, connectionpool));
        app.get("createuser",ctx -> ctx.render("createUser.html"));

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

    public static void createUser(Context ctx, ConnectionPool connectionPool) {
        //Hent formparametre
        String email = ctx.formParam("email");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");
        String roles = ctx.formParam("roles");
        String adress = ctx.formParam("adress");
        String phonenumber = ctx.formParam("phonenumber");


        if (password1.equals(password2)) {

            try {
                UserMapper.createuser(email, password1, roles, adress, phonenumber, connectionPool);
                ctx.attribute("message", "Brugeren oprettet med brugernavn: " + email + ". Log venligst på");
                ctx.render("login.html");

            } catch (DatabaseException e) {
                ctx.attribute("message", "Brugernavnet er allerede i brug");
                ctx.render("createUser.html");
            }

        } else {
            ctx.attribute("message", "Kodeordende matcher ikke");
            ctx.render("createUser.html");
        } ctx.render("login.html");
    }
}