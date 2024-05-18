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
                // test schema is already created, so we only need to delete/create test tables
                stmt.execute("DROP TABLE IF EXISTS test.users");
                stmt.execute("DROP SEQUENCE IF EXISTS test.users_user_id_seq CASCADE");
                // create table as copy of original public schema structure
                stmt.execute("CREATE TABLE test.users AS (SELECT * FROM public.users) WITH NO DATA");
                // create sequence for auto generating id's for users and orders
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

                // Remove all rows from all tables
                stmt.execute("DELETE FROM test.users");

                stmt.execute("INSERT INTO test.users (user_id, email, password, roles, adress, phonenumber)" +
                        "VALUES (1, 'admin@admin.dk', '1234', 'ADMIN', 'admingade1234', '12345678'), (2,'kunde@kunde.dk','1234','KUNDE','kundegade1234',123123123)");

                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('test.users_user_id_seq', COALESCE((SELECT MAX(user_id) +1 FROM test.users), 1), false)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Database connection failed");
        }

    }

    @Test
    void login() {
        // Test the login functionality
        String email = "admin@admin.dk";
        String password = "1234";

        try {
            User user = UserMapper.login(email, password, connectionPool);
            assertNotNull(user);
            assertEquals(email, user.getEmail());
            // Add more assertions as needed
        } catch (DatabaseException e) {
            fail("Exception occurred during login: " + e.getMessage());
        }
    }

    @Test
    void createuser() {

        // Define the expected user
        User expectedUser = new User("newuser@example.com", "newUserPassword", "KUNDER", "123 testgade", "1234567890");

        try {
            // Call the createuser method
            User insertedUser = UserMapper.createuser("newuser@example.com", "newUserPassword", "KUNDER", "123 testgade", "1234567890", connectionPool);

            // Assert that the properties of the insertedUser match the expectedUser
            assertEquals(expectedUser, insertedUser);
        } catch (DatabaseException e) {
            fail("Exception occurred during user creation: " + e.getMessage());
        }
    }
}