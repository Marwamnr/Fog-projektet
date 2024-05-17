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




public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        // Definer ruten til at vise ordrelisten
        app.get("/orderList", ctx ->   showOrderList(ctx, connectionPool));


        app.post("/Calculate", ctx -> {
            try {
                totalPrice(ctx, connectionPool);
                showOrderList(ctx, connectionPool);
    } catch (Exception e) {
        ctx.status(500).result("Error processing order status. Please try again.");
    }
});



        //app.get("/CustomerOrder", ctx -> showOrderListUser(ctx, connectionPool));
        app.post("/Inquiry", ctx -> createOrder(ctx, connectionPool));
        
        app.post("/confirm", ctx -> {
            try {
                Payment(ctx, connectionPool);
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
    }

    private static void Payment(Context ctx,  ConnectionPool connectionPool) {

        int orderId = Integer.parseInt(ctx.formParam("orderId"));

        int totalPrice = Integer.parseInt(ctx.formParam("totalPrice"));
         int amount=totalPrice;

        try {
            OrderMapper.createPayment(orderId, amount,connectionPool);
            ctx.attribute("message", "Betalingen er godkendt. Du kan se din stykliste");
            ctx.render("customerOrder.html");
            } catch (DatabaseException e) {
                ctx.attribute("message", "Noget gik galt. prøv igen");
                ctx.render("customerOrder.html");
            }
        }






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

    public static void showOrderListUser(Context ctx, ConnectionPool connectionPool) {
        try {
            // Retrieve the current user from the session
            User currentUser = ctx.sessionAttribute("currentUser");

            if (currentUser != null) {
                int userId = currentUser.getUserId();


                    // Hent ordrelisten for den specifikke bruger fra databasen
                    List<Order> orderList = OrderMapper.getAllOrdersUser(connectionPool, userId);
                    List<String> orderStatusList = OrderStatusMapper.getOrderStatus(connectionPool, userId);

                    // Tilføj ordrelisten til konteksten
                    ctx.attribute("orderList", orderList);
                    ctx.attribute("orderStatusList", orderStatusList);

                    // Rendere ordrelistevisningen
                    //ctx.render("customerOrder.html");


                } else {
                    // Handle the case when there is no logged-in user
                    ctx.status(401).result("Unauthorized"); // Or any appropriate action
                }
            } catch(Exception e){
                // Håndter eventuelle undtagelser
                ctx.status(500).result("Fejl ved hentning af ordreliste: " + e.getMessage());
            }
        }


    private static void createOrder(Context ctx, ConnectionPool connectionPool) {
        User currentUser = ctx.sessionAttribute("currentUser");


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



