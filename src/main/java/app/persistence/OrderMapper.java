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

    public static void createOrder(int user_id, int carportLength, int carportWidth, int toolroomLength, int toolroomWidth, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "insert into orders (orderstatus_id,user_id,toolroom_width,toolroom_length,total_price,carport_width, carport_length) values (?,?,?,?,?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, 1);


            ps.setInt(2, user_id);
            ps.setInt(3, toolroomWidth);
            ps.setInt(4, toolroomLength);
            ps.setInt(5, 0);
            ps.setInt(6, carportWidth);
            ps.setInt(7, carportLength);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af bestilling. Prøv igen");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            throw new DatabaseException(msg, e.getMessage());
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
}

