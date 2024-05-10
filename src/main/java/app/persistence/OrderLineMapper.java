package app.persistence;

import app.entities.OrderLine;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderLineMapper {

    public static List<OrderLine> getAllOrderLines(ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM public.order_line";
        List<OrderLine> orderLines = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int orderLineId = rs.getInt("order_line_id");
                int orderId = rs.getInt("order_id");
                int materialId = rs.getInt("material_id");
                String description = rs.getString("description");
                int quantity = rs.getInt("quantity");

                OrderLine orderLine = new OrderLine(orderLineId, orderId, materialId, description, quantity);
                orderLines.add(orderLine);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving order lines: " + e.getMessage());
        }
        return orderLines;
    }
}



