package app.controllers;

import app.entities.Order;
import app.entities.OrderLine;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderLineMapper;
import app.persistence.OrderMapper;
import app.services.Calculator;
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

        int status = 1; // Hardcoded for now
        int toolroomWidth = Integer.parseInt(ctx.formParam("toolroom_width"));
        int toolroomLength = Integer.parseInt(ctx.formParam("toolroom_length"));
        int carportWidth = Integer.parseInt(ctx.formParam("carport_width"));
        int carportLength = Integer.parseInt(ctx.formParam("carport_length"));
        int totalPrice = 19999; // Hardcoded for now


        // Create the order object
        Order order = new Order(0, status, currentUser.getUserId(), toolroomWidth, toolroomLength, totalPrice, carportWidth, carportLength);

        // TODO: Insert order in database
        try {

            // Insert the order into the database and get the newly inserted order
            order = OrderMapper.insertOrder(order, connectionPool);

            // TODO: Calculate order items (stykliste)
            Calculator calculator = new Calculator(carportWidth, carportLength, toolroomWidth, toolroomLength, connectionPool);
            calculator.calcCarport(order);

            // TODO: Save order items in database (stykliste)
            OrderLineMapper.createOrderLine(calculator.getOrderLines(), connectionPool);

            // TODO: Create message to customer and render order / request confirmation
            ctx.render("orderConfirmation.html");
        } catch (DatabaseException e) {
            // Handle database exception
            // You can customize the error message or handle it in any way appropriate for your application
            ctx.attribute("message", "Noget gik galt. prøv igen");
            ctx.render("designCarport.html");
        }
    }
}



