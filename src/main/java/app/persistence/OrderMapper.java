package app.persistence;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import io.javalin.http.Context;

public class OrderMapper {
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
}


