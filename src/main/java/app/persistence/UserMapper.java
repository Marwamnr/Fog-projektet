package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;

public class UserMapper {
    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        //Der udføres
        String sql = "select * from users where email=? and password=?";

        try (
                //oprettes en forbindelse til databasen
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            //Det bliver sat som parametre
            ps.setString(1, email);
            ps.setString(2, password);


            //sql bliver udført og gemme resultatet
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int user_id = rs.getInt("user_id");
                String roles = rs.getString("roles");
                String adress = rs.getString("adress");
                String phonenumber = rs.getString("phonenumber");

                return new User(user_id, email, password, roles, adress, phonenumber);
            } else {
                throw new DatabaseException("Fejl i login. Prøv igen");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl!", e.getMessage());
        }
    }

    public static User createuser(String email, String password, String roles, String adress, String phonenumber, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "insert into users (email, password,roles,adress,phonenumber) values (?,?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, roles);
            ps.setString(4, adress);
            ps.setString(5, phonenumber);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af ny bruger");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    User newUser = new User(email, password, roles, adress, phonenumber);
                    newUser.setUserId(userId);
                    return newUser;
                } else {
                    throw new DatabaseException("Fejl ved oprettelse af bruger. Bruger-ID blev ikke genereret");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Der er sket en fejl. Prøv igen", e.getMessage());
        }
    }
}


