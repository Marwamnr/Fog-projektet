package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

// integrations test
class UserMapperTest {

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance("", "", "", "");

    @BeforeAll
    static void setupClass() {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement stmt = connection.createStatement()) {

                stmt.execute("DROP TABLE IF EXISTS test.users");
                stmt.execute("DROP SEQUENCE IF EXISTS test.users_user_id_seq CASCADE");

                stmt.execute("CREATE TABLE test.users AS (SELECT * FROM public.users) WITH NO DATA");

                stmt.execute("CREATE SEQUENCE test.users_user_id_seq");
                stmt.execute("ALTER TABLE test.users ALTER COLUMN user_id SET DEFAULT nextval('test.users_user_id_seq')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Database connection failed");
        }
    }

    @BeforeEach
    void setUp() {

        try (Connection connection = connectionPool.getConnection()) {
            try (Statement stmt = connection.createStatement()) {


                stmt.execute("DELETE FROM test.users");

                stmt.execute("INSERT INTO test.users (user_id, email, password, roles, adress, phonenumber)" +
                        "VALUES (1, 'admin@admin.dk', '1234', 'ADMIN', 'admingade1234', '12345678'), (2,'kunde@kunde.dk','1234','KUNDER','kundegade1234',123123123)");


                stmt.execute("SELECT setval('test.users_user_id_seq', COALESCE((SELECT MAX(user_id) +1 FROM test.users), 1), false)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Database connection failed");
        }

    }

    @Test
    void login() {

        String email = "admin@admin.dk";
        String password = "1234";

        try {
            User user = UserMapper.login(email, password, connectionPool);
            assertNotNull(user);
            assertEquals(email, user.getEmail());

        } catch (DatabaseException e) {
            fail("Exception occurred during login: " + e.getMessage());
        }
    }

    @Test
    void createuser() {

        try {

            User newUser = new User("newuser@newuser.dk", "1234", "KUNDER", "newusergade1234", "87654321");

            User insertedUser = UserMapper.createuser(
                    newUser.getEmail(),
                    newUser.getPassword(),
                    newUser.getRoles(),
                    newUser.getAdress(),
                    newUser.getPhonenumber(),
                    connectionPool);

            newUser.setUserId(insertedUser.getUserId());

            assertEquals(newUser, insertedUser);
        } catch (DatabaseException e) {
            fail("Exception occurred during user creation: " + e.getMessage());
        }
    }
}