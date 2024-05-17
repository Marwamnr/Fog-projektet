package app.persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM public.orders";
        List<Order> orderList = new ArrayList<>();


        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);

        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int orderStatusId = rs.getInt("orderstatus_id");
                int userId = rs.getInt("user_id");
                int toolroomWidth = rs.getInt("toolroom_width");
                int toolroomLength = rs.getInt("toolroom_length");
                int totalPrice = rs.getInt("total_price");
                int carportWidth = rs.getInt("carport_width");
                int carportLength = rs.getInt("carport_length");

                Order order = new Order(orderId, orderStatusId, userId, toolroomWidth, toolroomLength, totalPrice, carportWidth, carportLength);
                orderList.add(order);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving orders: " + e.getMessage());
        }
        return orderList;
    }

    public static List<Order> getAllOrdersUser(ConnectionPool connectionPool, int userId) throws DatabaseException {
        String sql = "SELECT * FROM public.orders WHERE user_id = ?";
        List<Order> orderList = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int orderStatusId = rs.getInt("orderstatus_id");
                int toolroomWidth = rs.getInt("toolroom_width");
                int toolroomLength = rs.getInt("toolroom_length");
                int totalPrice = rs.getInt("total_price");
                int carportWidth = rs.getInt("carport_width");
                int carportLength = rs.getInt("carport_length");

                Order order = new Order(orderId, orderStatusId, userId, toolroomWidth, toolroomLength, totalPrice, carportWidth, carportLength);
                orderList.add(order);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving orders: " + e.getMessage());
        }
        return orderList;
    }

    public static Order insertOrder(Order order, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO public.orders (orderstatus_id, user_id, toolroom_width, toolroom_length, total_price, carport_width, carport_length) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, order.getOrderStatusId());
                ps.setInt(2, order.getUserId());
                ps.setInt(3, order.getToolroomWidth());
                ps.setInt(4, order.getToolroomLength());
                ps.setInt(5, order.getTotalPrice());
                ps.setInt(6, order.getCarportWidth());
                ps.setInt(7, order.getCarportLength());
                ps.executeUpdate();

                ResultSet keySet = ps.getGeneratedKeys();
                if (keySet.next()) {

                    Order newOrder = new Order(keySet.getInt(1), order.getOrderStatusId(), order.getUserId(), order.getToolroomWidth(),
                            order.getToolroomLength(), order.getTotalPrice(), order.getCarportWidth(), order.getCarportLength());
                    return newOrder;
                } else
                    return null;
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Could not create user in the database", e.getMessage());
        }
    }
    public static void createPayment(int orderId, int amount, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO payments (order_id, amount) VALUES (?, ?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, orderId);
            ps.setInt(2, amount);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af betaling. Prøv igen");
            }


            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int paymentId = generatedKeys.getInt(1);
                } else {
                    throw new DatabaseException("Fejl ved oprettelse af betaling. Ingen genereret betalings-id blev returneret");
                }
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            throw new DatabaseException(msg, e.getMessage());
        }

    }


    public static boolean checkPayment(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM payments WHERE order_id = ? AND amount IS NOT NULL";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            String errorMessage = "Error checking payment for order ID " + orderId + ": " + e.getMessage();
            throw new DatabaseException(errorMessage);
        }
    }

    public static int calculateTotalPrice(ConnectionPool connectionPool, int orderId) throws DatabaseException {
        String sql = "SELECT SUM(ol.quantity * m.meter_price) AS total_price " +
                "FROM public.order_line ol " +
                "INNER JOIN public.material m ON ol.material_id = m.material_id " +
                "INNER JOIN public.orders o ON ol.order_id = o.order_id " +
                "WHERE o.order_id = ?";
        int totalPrice = 0;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalPrice = rs.getInt("total_price");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indhentning af totalpris for " + orderId + ": " + e.getMessage());
        }
        return totalPrice;
    }

    public static void saveTotalPrice(ConnectionPool connectionPool, int orderId) throws DatabaseException {

        int totalPrice = calculateTotalPrice(connectionPool,orderId);

        String sql = "UPDATE public.orders SET total_price = ? WHERE order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, totalPrice);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error saving total price for order " + orderId + ": " + e.getMessage());
        }
    }
}
