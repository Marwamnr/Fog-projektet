package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController {

    public static void addRoutes(Javalin app, ConnectionPool connectionpool) {
        //app.post("/login", ctx -> login(ctx, connectionpool));
        //app.get("logout", ctx->logout(ctx));
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

            //ctx.attribute("toppingList", ToppingMapper.getToppings(connectionPool));
            //ctx.attribute("bundList", BundMapper.getBunds(connectionPool));
            ctx.render("cupcake.html");

        } catch (DatabaseException e) {

            ctx.attribute("message", e.getMessage());


            ctx.render("index.html");
        }
    }
}