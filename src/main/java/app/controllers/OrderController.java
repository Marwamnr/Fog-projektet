package app.controllers;



import app.entities.Order;
import app.entities.OrderLine;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderLineMapper;
import app.persistence.OrderMapper;
import app.persistence.OrderStatusMapper;
import app.services.Calculator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

import static app.controllers.OrderStatusController.updateOrderStatus;


public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/orderList", ctx -> showOrderList(ctx, connectionPool));
        app.post("/Inquiry", ctx -> createOrder(ctx, connectionPool));

        app.post("/confirm", ctx -> {
            try {
                Payment(ctx, connectionPool);
                updateOrderStatus(ctx, connectionPool);
                showOrderListUser(ctx, connectionPool);
                ctx.render("customerOrder.html");
            } catch (Exception e) {
                ctx.status(500).result("Error processing order status. Please try again.");
            }
        });


        app.get("/Status", ctx -> {
            try {
                showOrderListUser(ctx, connectionPool);
                ctx.render("customerOrder.html");
            } catch (Exception e) {
                ctx.status(500).result("Error processing order status. Please try again.");
            }
        });

        app.post("/Calculate", ctx -> {
            try {
                updateOrderStatus(ctx, connectionPool);
                totalPrice(ctx, connectionPool);
                showOrderList(ctx, connectionPool);
            } catch (Exception e) {
                ctx.status(500).result("Error processing order status. Please try again.");
            }
        });
    }

    private static void Payment(Context ctx,  ConnectionPool connectionPool) throws DatabaseException {

        int orderId = Integer.parseInt(ctx.formParam("orderId"));

        int amount = OrderMapper.calculateTotalPrice(connectionPool,orderId);

        try {
            OrderMapper.createPayment(orderId, amount,connectionPool);
            ctx.attribute("message", "Betalingen er godkendt. Du kan se din stykliste");
            ctx.render("customerOrder.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Noget gik galt. prøv igen");
            ctx.render("customerOrder.html");
        }
    }


    public static void showOrderList(Context ctx, ConnectionPool connectionPool) {
        try {
            List<Order> orderList = OrderMapper.getAllOrders(connectionPool);
            List<OrderLine> orderLines = OrderLineMapper.getAllOrderLines(connectionPool);


            ctx.attribute("orderList", orderList);
            ctx.attribute("orderLines", orderLines);

            ctx.render("orderOverview.html");
        } catch (Exception e) {
            // Håndter eventuelle undtagelser
            ctx.status(500).result("Fejl ved hentning af ordreliste eller linjer: " + e.getMessage());
        }
    }

    public static void showOrderListUser(Context ctx, ConnectionPool connectionPool) {
        try {
            User currentUser = ctx.sessionAttribute("currentUser");

            if (currentUser != null) {
                int userId = currentUser.getUserId();


                List<Order> orderList = OrderMapper.getAllOrdersUser(connectionPool, userId);
                List<String> getOrderStatus = OrderStatusMapper.getOrderStatus(connectionPool, userId);

                ctx.attribute("orderList", orderList);
                ctx.attribute("orderStatusList", getOrderStatus);

                ctx.render("customerOrder.html");

            } else {
                ctx.status(401).result("Unauthorized");
            }
        } catch(Exception e){
            ctx.status(500).result("Fejl ved hentning af ordreliste: " + e.getMessage());
        }
    }


    private static void createOrder(Context ctx, ConnectionPool connectionPool) {

        User currentUser = ctx.sessionAttribute("currentUser");
        if (currentUser == null) {
            ctx.attribute("message", "Du skal logge på for at oprette en bestilling.");
            ctx.render("designCarport.html");
            return;
        }

        int status = 1;
        int toolroomWidth = Integer.parseInt(ctx.formParam("toolroom_width"));
        int toolroomLength = Integer.parseInt(ctx.formParam("toolroom_length"));
        int carportWidth = Integer.parseInt(ctx.formParam("carport_width"));
        int carportLength = Integer.parseInt(ctx.formParam("carport_length"));
        int totalPrice = 0;


        Order order = new Order(0, status, currentUser.getUserId(), toolroomWidth, toolroomLength, totalPrice, carportWidth, carportLength);

        try {

            order = OrderMapper.insertOrder(order, connectionPool);


            Calculator calculator = new Calculator(carportWidth, carportLength, toolroomWidth, toolroomLength, connectionPool);
            calculator.calcCarport(order);

            OrderLineMapper.createOrderLine(calculator.getOrderLines(), connectionPool);

            ctx.render("Orderinquiry.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Noget gik galt. prøv igen");
            ctx.render("designCarport.html");
        }
    }

    private static void totalPrice(Context ctx, ConnectionPool connectionPool) {

        int orderId = Integer.parseInt(ctx.formParam("orderId"));

        try {
            OrderMapper.calculateTotalPrice(connectionPool,orderId);
            OrderMapper.saveTotalPrice(connectionPool,orderId);
        } catch (DatabaseException e) {
            ctx.attribute("message", "Noget gik galt med totalpris. prøv igen");

        }
    }
}