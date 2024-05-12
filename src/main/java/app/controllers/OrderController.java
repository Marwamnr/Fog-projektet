package app.controllers;

import app.entities.Order;
import app.entities.OrderLine;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderLineMapper;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/orderList", ctx -> showOrderList(ctx, connectionPool));

        app.post("/Inquiry", ctx -> createOrder(ctx, connectionPool));
    }

    public static void showOrderList(Context ctx, ConnectionPool connectionPool) {
        try {
            // Hent ordrelisten fra databasen
            List<Order> orderList = OrderMapper.getAllOrders(connectionPool);
            List<OrderLine> orderLines = OrderLineMapper.getAllOrderLines(connectionPool);

            // Tilføj ordrelisten til konteksten
            ctx.attribute("orderList", orderList);
            ctx.attribute("orderLines", orderLines);

            // Rendere ordrelistevisningen
            ctx.render("orderOverview.html");
        } catch (Exception e) {
            // Håndter eventuelle undtagelser
            ctx.status(500).result("Fejl ved hentning af ordreliste eller linjer: " + e.getMessage());
        }
    }

    private static void createOrder(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null) {
            ctx.attribute("message", "Du skal logge på for at oprette en bestilling.");
            ctx.render("designCarport.html");
            return;
        }

        int user_id = currentUser.getUserId();
        int carport_length = Integer.parseInt(ctx.formParam("carport_length"));
        int carport_width = Integer.parseInt(ctx.formParam("carport_width"));
        int toolroom_length = Integer.parseInt(ctx.formParam("toolroom_length"));
        int toolroom_width = Integer.parseInt(ctx.formParam("toolroom_width"));


        try {
            OrderMapper.createOrder(user_id, carport_length, carport_width, toolroom_length, toolroom_width, connectionPool);
            ctx.render("orderConfirmation.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Noget gik galt. prøv igen");
            ctx.render("designCarport.html");
        }
    }
}



