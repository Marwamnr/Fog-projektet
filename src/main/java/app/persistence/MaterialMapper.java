package app.persistence;

import app.entities.Material;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MaterialMapper {
        String sql = "SELECT * FROM material WHERE group_id = ? AND length >= ?";
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, groupId);
            ps.setInt(2, minLength);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int material_id = resultSet.getInt("material_id");
                int width = resultSet.getInt("width");
                int length = resultSet.getInt("length");
                int meterPrice = resultSet.getInt("meter_price");
                String description = resultSet.getString("description");
                String unit = resultSet.getString("unit");
                int group = resultSet.getInt("group_id");
                Material material = new Material(material_id, width, length, meterPrice, description, unit);

                
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not get materials from the database", e.getMessage());
        }
        return materials;
    }
}



