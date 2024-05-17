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
        String sql = "SELECT os.status_name " +
                "FROM public.order_status os " +
                "INNER JOIN public.orders o ON os.orderstatus_id = o.orderstatus_id " +
                "WHERE o.user_id = ?";
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

    public static void updateOrderStatus(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String selectSql = "SELECT orderstatus_id FROM orders WHERE order_id = ?";
        String updateSql = "UPDATE orders SET orderstatus_id = ? WHERE order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement psSelectStatus = connection.prepareStatement(selectSql);
             PreparedStatement psUpdateStatus = connection.prepareStatement(updateSql)) {

            // Fetch the current order status
            psSelectStatus.setInt(1, orderId);
            ResultSet rs = psSelectStatus.executeQuery();
            if (rs.next()) {
                int currentStatusId = rs.getInt("orderstatus_id");
                int newStatusId = currentStatusId + 1;

                // Update to the new order status
                psUpdateStatus.setInt(1, newStatusId);
                psUpdateStatus.setInt(2, orderId);

                int rowsAffected = psUpdateStatus.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Error updating order status. Please try again.");
                }
            } else {
                throw new DatabaseException("Order not found. Please try again.");
            }

        } catch (SQLException e) {
            String msg = "An error occurred while updating order status. Please try again.";
            throw new DatabaseException(msg);
        }
    }


}