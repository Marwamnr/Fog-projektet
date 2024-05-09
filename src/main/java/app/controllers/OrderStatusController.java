package app.controllers;

import app.entities.PartListItem;
import app.persistence.ConnectionPool;
import app.persistence.OrderStatusMapper;
import app.persistence.OrderLineMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class OrderStatusController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/orderStatus", ctx -> ctx.render("orderStatus.html"));
        app.post("/orderStatus", ctx -> showOrderLines(ctx, connectionPool));
    }

    public static void showOrderLines(Context ctx, ConnectionPool connectionPool) {
        try {
            int userId = ctx.sessionAttribute("userId");

            List<PartListItem> orderLines = OrderLineMapper.getOrderLinesForUser(connectionPool, userId);

            List<String> orderStatusList = OrderStatusMapper.getOrderStatus(connectionPool, userId);

            ctx.attribute("orderLines", orderLines);
            ctx.attribute("orderStatusList", orderStatusList);

            ctx.render("orderStatus.html");
        } catch (Exception e) {
            ctx.status(500).result("Error displaying order lines: " + e.getMessage());
        }
    }
}

