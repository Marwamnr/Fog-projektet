package app.controllers;

import app.entities.PartListItem;
import app.persistence.ConnectionPool;
import app.persistence.OrderStatusMapper;
import app.persistence.PartListMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class OrderStatusController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        // Define route to show order lines and order status
        app.get("/orderLines", ctx -> showOrderLines(ctx, connectionPool));
    }

    public static void showOrderLines(Context ctx, ConnectionPool connectionPool) {
        try {
            // Retrieve user ID from the session
            int userId = ctx.sessionAttribute("userId");

            // Retrieve order lines from the database
            List<PartListItem> partListItems = PartListMapper.getPartListForUser(connectionPool, userId);

            // Retrieve order status from the database
            List<String> orderStatusList = OrderStatusMapper.getOrderStatus(connectionPool, userId);

            // Add order lines and order status to the context
            ctx.attribute("partListItems", partListItems);
            ctx.attribute("orderStatusList", orderStatusList);

            // Render order lines view
            ctx.render("orderStatus.html");
        } catch (Exception e) {
            // Handle any exceptions
            ctx.status(500).result("Error displaying order lines: " + e.getMessage());
        }
    }
}

