package app.controllers;

import app.entities.PartList;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderLineMapper;
import app.persistence.OrderStatusMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class OrderStatusController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/orderStatus", ctx -> showOrderLines(ctx, connectionPool));
        app.post("/orderStatus", ctx -> showOrderLines(ctx, connectionPool));


        app.post("/Calculate", ctx -> {
            int orderId = Integer.parseInt(ctx.queryParam("order_id"));
            try {
                // Kall på updateOrderStatusTo2-metoden fra OrderMapper-klassen
                OrderStatusMapper.updateOrderStatusTo(orderId, connectionPool);
                // Sett HTTP-status 200 OK som respons
                ctx.status(200).result("Order status updated to 2 successfully.");
            } catch (DatabaseException e) {
                // Håndter unntak og sett riktig HTTP-statuskode ved feil
                e.printStackTrace();
                ctx.status(500).result("Error updating order status. Please try again.");
            }
        });
    }

    public static void showOrderLines(Context ctx, ConnectionPool connectionPool) {
        try {

            int orderId = Integer.parseInt(ctx.formParam("orderId"));

           List<PartList> partList = OrderLineMapper.PartList(connectionPool, orderId);

            List<String> orderStatusList = OrderStatusMapper.getOrderStatus(connectionPool, orderId);

           ctx.attribute("partList", partList);
            ctx.attribute("orderStatusList", orderStatusList);

            ctx.render("orderStatus.html");
        } catch (Exception e) {
            ctx.status(500).result("Error displaying order lines: " + e.getMessage());
        }
    }
}

