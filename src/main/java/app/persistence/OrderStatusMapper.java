package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusMapper {

    public static List<String> getOrderStatus(ConnectionPool connectionPool, int userId) throws DatabaseException {
        String sql = "SELECT status_name FROM public.order_status WHERE orderstatus_id IN (SELECT orderstatus_id FROM public.orders WHERE user_id = ?)";
        List<String> orderStatusList = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String statusName = rs.getString("status_name");
                orderStatusList.add(statusName);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving order status for user " + userId + ": " + e.getMessage());
        }
        return orderStatusList;
    }
}

