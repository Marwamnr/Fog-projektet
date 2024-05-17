package app.persistence;

import app.entities.OrderLine;
import app.entities.PartList;
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


    public static List<PartList> PartList(ConnectionPool connectionPool, int orderId) throws DatabaseException {
        List<PartList> partLists = new ArrayList<>();
        String sql = "SELECT m.length, m.description AS material_description, m.unit, ol.description AS order_line_description, ol.quantity " +
                "FROM public.material m " +
                "INNER JOIN public.order_line ol ON m.material_id = ol.material_id " +
                "WHERE ol.order_id = ?;";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement prepareStatement = connection.prepareStatement(sql);
        ) {
            prepareStatement.setInt(1, orderId);
            try (ResultSet rs = prepareStatement.executeQuery()) {
                while (rs.next()) {
                    String materialDescription = rs.getString("material_description");
                    int length = rs.getInt("length");
                    int quantity = rs.getInt("quantity");
                    String unit = rs.getString("unit");
                    String orderLineDescription = rs.getString("order_line_description");


                    PartList partList = new PartList(materialDescription, length, quantity, unit, orderLineDescription);
                    partLists.add(partList);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving part lists: " + e.getMessage());
        }
        return partLists;
    }

    public static void createOrderLine(List<OrderLine> orderLines, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "INSERT INTO order_line (order_id, material_id, description, quantity) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = connectionPool.getConnection()) {

            for (OrderLine orderLine : orderLines) {
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setInt(1, orderLine.getOrderId());
                    ps.setInt(2, orderLine.getMaterialId());
                    ps.setString(3, orderLine.getDescription());
                    ps.setInt(4, orderLine.getQuantity());
                    ps.executeUpdate();

                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not create orderline in the database: " + e.getMessage());
        }
    }

}



