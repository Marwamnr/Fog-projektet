package app.persistence;

import app.entities.PartListItem;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartListMapper {

    public static List<PartListItem> getPartListForUser(ConnectionPool connectionPool, int userId) throws DatabaseException {
        String sql = "SELECT m.description AS material_description, m.length, ol.quantity, m.unit, ol.description AS orderline_description " +
                "FROM public.material m " +
                "JOIN public.order_line ol ON m.material_id = ol.material_id " +
                "JOIN public.orders o ON ol.order_id = o.order_id " +
                "WHERE o.user_id = ?";
        List<PartListItem> partListItems = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String materialDescription = rs.getString("material_description");
                int length = rs.getInt("length");
                int quantity = rs.getInt("quantity");
                String unit = rs.getString("unit");
                String orderLineDescription = rs.getString("orderline_description");

                PartListItem partListItem = new PartListItem(materialDescription, length, quantity, unit, orderLineDescription);
                partListItems.add(partListItem);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving part list items for user " + userId + ": " + e.getMessage());
        }
        return partListItems;
    }
}