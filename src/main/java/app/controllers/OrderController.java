package app.controllers;

import app.persistence.OrderMapper;
import app.persistence.ConnectionPool;
import app.exceptions.DatabaseException;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionpool) {
        app.post("/Inquiry", ctx->createOrder(ctx, connectionpool));
    }

    private static void createOrder(Context ctx, ConnectionPool connectionPool){
    int carport_length=Integer.parseInt(ctx.formParam("carport_length"));
    int carport_width=Integer.parseInt(ctx.formParam("carport_width"));
    int toolroom_length=Integer.parseInt(ctx.formParam("toolroom_length"));
    int toolroom_width=Integer.parseInt(ctx.formParam("toolroom_width"));

    int user_id = ctx.sessionAttribute("user_id");

        try {
            OrderMapper.createOrder(user_id,carport_length, carport_width,toolroom_length, toolroom_width, connectionPool);
            ctx.attribute("message", "Din bestilling er oprettet. Du hører fra os snarest.");
            ctx.render("login.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Noget gik galt. prøv igen");
            ctx.render("login.html");
        }

    }
}
