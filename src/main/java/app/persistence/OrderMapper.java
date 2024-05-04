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
}