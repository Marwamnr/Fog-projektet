package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper
{
    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException
    {
        //Der udføres
        String sql = "select * from users where email=? and password=?";

        try (
                //oprettes en forbindelse til databasen
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            //Det bliver sat som parametre
            ps.setString(1, email);
            ps.setString(2, password);

            //sql bliver udført og gemme resultatet
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                int user_id = rs.getInt("user_id");
                String role = rs.getString("role");
                return new User(user_id, email, password,role);
            } else
            {
                throw new DatabaseException("Fejl i login. Prøv igen");
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("DB fejl!", e.getMessage());
        }
    }
}