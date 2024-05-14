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

        app.post("/orderStatus", ctx -> {
            try {
                updateOrderStatus(ctx, connectionPool);
                showOrderLines(ctx, connectionPool);
            } catch (Exception e) {
                ctx.status(500).result("Error processing order status. Please try again.");
            }
        });
        //app.get("/Status", ctx -> ctx.render("orderOverview.html"));

    }




    public static void updateOrderStatus(Context ctx, ConnectionPool connectionPool) {
        int orderId = Integer.parseInt(ctx.formParam("orderId"));
        try {
            OrderStatusMapper.updateOrderStatus(orderId, connectionPool);
            List<String> orderStatus = OrderStatusMapper.getOrderStatus(connectionPool, orderId);
            ctx.attribute("orderStatus", orderStatus);
            ctx.status(200).result("Order status updated successfully.");
        } catch (DatabaseException e) {
            e.printStackTrace();
            ctx.status(500).result("Error updating order status. Please try again.");
        }
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
