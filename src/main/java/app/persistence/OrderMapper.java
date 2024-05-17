package app.persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM public.orders " + "ORDER BY order_id DESC";
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

}