package app.controllers;

import app.entities.PartList;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderLineMapper;
import app.persistence.OrderMapper;
import app.persistence.OrderStatusMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderStatusMapper;
import app.persistence.OrderLineMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

public class OrderStatusController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {

        app.post("/PartListAdmin", ctx -> {
            try {
                updateOrderStatus(ctx, connectionPool);
                showOrderLinesAdmin(ctx, connectionPool);
            } catch (Exception e) {
                ctx.status(500).result("Error processing order status. Please try again.");
            }
        });

        app.post("/PartListCustomer", ctx -> {
            try {
                updateOrderStatus(ctx, connectionPool);
                showOrderLinesCustomer(ctx, connectionPool);
            } catch (Exception e) {
                ctx.status(500).result("Error processing order status. Please try again.");
            }
        });


    }


    public static void updateOrderStatus(Context ctx, ConnectionPool connectionPool) {
        int orderId = Integer.parseInt(ctx.formParam("orderId"));
        try {
            OrderStatusMapper.updateOrderStatus(orderId, connectionPool);
            List<String> orderStatus = OrderStatusMapper.getOrderStatus(connectionPool, orderId);
        app.get("/orderStatus", ctx -> showOrderLines(ctx, connectionPool));
        app.post("/orderStatus", ctx -> {
            try {
                updateOrderStatus(ctx, connectionPool);
                showOrderLines(ctx, connectionPool);
            } catch (Exception e) {
                ctx.status(500).result("Error processing order status. Please try again.");
              
        app.get("/orderStatus", ctx -> ctx.render("orderStatus.html"));
        app.post("/orderStatus", ctx -> showOrderLines(ctx, connectionPool));

        app.post("/Calculate", ctx -> {
            int orderId = Integer.parseInt(ctx.queryParam("order_id"));
            try {
                OrderStatusMapper.updateOrderStatusTo(orderId, connectionPool);
                ctx.status(200).result("Order status updated to 2 successfully.");
            } catch (DatabaseException e) {
                e.printStackTrace();
                ctx.status(500).result("Error updating order status. Please try again.");
            }
        });

    }

    public static void updateOrderStatus(Context ctx, ConnectionPool connectionPool){
        int orderId = Integer.parseInt(ctx.formParam("orderId"));
        try {
            OrderStatusMapper.updateOrderStatusTwo(orderId, connectionPool);
            List<String> orderStatus=OrderStatusMapper.getOrderStatus(connectionPool,orderId);
            ctx.attribute("orderStatus", orderStatus);
            ctx.status(200).result("Order status updated successfully.");
        } catch (DatabaseException e) {
            e.printStackTrace();
            ctx.status(500).result("Error updating order status. Please try again.");
        }
    }


    public static void showOrderLinesCustomer(Context ctx, ConnectionPool connectionPool) {

        int orderId = Integer.parseInt(ctx.formParam("orderId"));

        try {
            boolean payment = OrderMapper.checkPayment(orderId, connectionPool);

            if (payment) {

                try {

                    List<PartList> partList = OrderLineMapper.PartList(connectionPool, orderId);

                    List<String> orderStatusList = OrderStatusMapper.getOrderStatus(connectionPool, orderId);

                    ctx.attribute("partList", partList);
                    ctx.attribute("orderStatusList", orderStatusList);
                    ctx.render("orderStatus.html");
                } catch (Exception e) {
                    ctx.status(500).result("Fejl ved hentning og visning af ordrelinjer: " + e.getMessage());
                }
            } else {
                ctx.status(401).result("Uautoriseret: Ingen betaling fundet for ordre ID " + orderId);
            }
        } catch (Exception e) {
            ctx.status(500).result("Fejl ved behandling af anmodning: " + e.getMessage());
        }
    }

    public static void showOrderLinesAdmin(Context ctx, ConnectionPool connectionPool) {

        int orderId = Integer.parseInt(ctx.formParam("orderId"));
                try {

                    List<PartList> partList = OrderLineMapper.PartList(connectionPool, orderId);

                    List<String> orderStatusList = OrderStatusMapper.getOrderStatus(connectionPool, orderId);

                    ctx.attribute("partList", partList);
                    ctx.attribute("orderStatusList", orderStatusList);
                    ctx.render("orderStatus.html");
                } catch (Exception e) {
                    ctx.status(500).result("Fejl ved hentning og visning af ordrelinjer: " + e.getMessage());
                }
            }
    }


    public static void showOrderLines(Context ctx, ConnectionPool connectionPool) {
        try {

            int orderId = Integer.parseInt(ctx.formParam("orderId"));

            List<PartList> partList = OrderLineMapper.PartList(connectionPool, orderId);

            if (partList.isEmpty())
            {
                ctx.render("orderStatus.html");
                return;
            }

            List<String> orderStatusList = OrderStatusMapper.getOrderStatus(connectionPool, orderId);

            ctx.attribute("partList", partList);
            ctx.attribute("orderStatusList", orderStatusList);



            ctx.render("orderStatus.html");
        } catch (Exception e) {
            ctx.status(500).result("Error displaying order lines: " + e.getMessage());
        }
    }
}
