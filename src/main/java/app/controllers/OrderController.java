package app.controllers;

import app.entities.Order;
import app.entities.OrderLine;
import app.persistence.ConnectionPool;
import app.persistence.OrderLineMapper;
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

}


