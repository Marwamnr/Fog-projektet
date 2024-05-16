package app.persistence;

import app.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusMapper {
    public static List<String> getOrderStatus(ConnectionPool connectionPool, int user_id ) throws DatabaseException {
        String sql = "SELECT os.status_name " +
                "FROM public.order_status os " +
                "INNER JOIN public.orders o ON os.orderstatus_id = o.orderstatus_id " +
                "WHERE o.user_id = ?";
        List<String> orderStatusList = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, user_id );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String statusName = rs.getString("status_name");
                orderStatusList.add(statusName);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving order status for user " + user_id  + ": " + e.getMessage());
        }
        return orderStatusList;
    }

    public static void updateOrderStatus(int orderId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE orders SET orderstatus_id = 2 WHERE order_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement psUpdateStatus = connection.prepareStatement(sql)) {

            psUpdateStatus.setInt(1, orderId);

            int rowsAffected = psUpdateStatus.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Error updating order status. Please try again.");
            }
        } catch (SQLException e) {
            String msg = "An error occurred while updating order status. Please try again.";
            throw new DatabaseException(msg, e.getMessage());
        }
    }
}