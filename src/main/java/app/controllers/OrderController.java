package app.controllers;

import app.entities.Order;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        // Definer ruten til at vise ordrelisten
        app.get("/orderList", ctx -> showOrderList(ctx, connectionPool));
    }

    public static void showOrderList(Context ctx, ConnectionPool connectionPool) {
        try {
            // Hent ordrelisten fra databasen
            List<Order> orderList = OrderMapper.getAllOrders(connectionPool);

            // Tilføj ordrelisten til konteksten
            ctx.attribute("orderList", orderList);

            // Rendere ordrelistevisningen
            ctx.render("orderOverview.html");
        } catch (Exception e) {
            // Håndter eventuelle undtagelser
            ctx.status(500).result("Fejl ved hentning af ordreliste: " + e.getMessage());
        }
    }

    private static void createOrder(Context ctx, ConnectionPool connectionPool){

        int user_id = Integer.parseInt(ctx.formParam("user_id"));

        int carport_length=Integer.parseInt(ctx.formParam("carport_length"));
        int carport_width=Integer.parseInt(ctx.formParam("carport_width"));
        int toolroom_length=Integer.parseInt(ctx.formParam("toolroom_length"));
        int toolroom_width=Integer.parseInt(ctx.formParam("toolroom_width"));

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




