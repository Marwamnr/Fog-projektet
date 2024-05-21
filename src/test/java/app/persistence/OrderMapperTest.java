package app.persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// integrations test
class OrderMapperTest {

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance("", "", "", "");

    @BeforeAll
    static void setupClass() {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                // test schema is already created, so we only need to delete/create test tables
                stmt.execute("DROP TABLE IF EXISTS test.users");
                stmt.execute("DROP TABLE IF EXISTS test.orders");
                stmt.execute("DROP SEQUENCE IF EXISTS test.users_user_id_seq CASCADE");
                stmt.execute("DROP SEQUENCE IF EXISTS test.orders_order_id_seq CASCADE");
                // create table as copy of original public schema structure
                stmt.execute("CREATE TABLE test.users AS (SELECT * FROM public.users) WITH NO DATA");
                stmt.execute("CREATE TABLE test.orders AS (SELECT * FROM public.orders) WITH NO DATA");
                // create sequence for auto generating id's for users and orders
                stmt.execute("CREATE SEQUENCE test.users_user_id_seq");
                stmt.execute("ALTER TABLE test.users ALTER COLUMN user_id SET DEFAULT nextval('test.users_user_id_seq')");
                stmt.execute("CREATE SEQUENCE test.orders_order_id_seq");
                stmt.execute("ALTER TABLE test.orders ALTER COLUMN order_id SET DEFAULT nextval('test.orders_order_id_seq')");
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
                stmt.execute("DELETE FROM test.orders");
                stmt.execute("DELETE FROM test.users");

                stmt.execute("INSERT INTO test.users (user_id, email, password, roles, adress, phonenumber)" +
                        "VALUES (1, 'admin@admin.dk', '1234', 'ADMIN', 'admingade1234', '12345678'), (2,'kunde@kunde.dk','1234','KUNDE','kundegade1234',123123123)");

                stmt.execute("INSERT INTO test.orders (order_id, orderstatus_id, user_id, toolroom_width, toolroom_length, total_price, carport_width, carport_length) " +
                        "VALUES (1, 2, 2, 0, 0, 1100, 240, 240), (2, 2, 2, 0, 0, 1100, 240, 240), (3, 2, 1, 0, 420, 19999, 240, 600)");


                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('test.orders_order_id_seq', COALESCE((SELECT MAX(order_id) +1 FROM test.orders), 1), false)");
                stmt.execute("SELECT setval('test.users_user_id_seq', COALESCE((SELECT MAX(user_id) +1 FROM test.users), 1), false)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Database connection failed");
        }

    }

    @Test
    void getAllOrders() {

        try {
            int expected = 3;
            List<Order> actualOrders = OrderMapper.getAllOrders(connectionPool);
            assertEquals(expected, actualOrders.size());
        } catch (DatabaseException e) {
            fail("Database fejl" + e.getMessage());
        }
    }


    @Test
    void getAllOrdersUser() {

        try {
            // Get all orders for a specific user (user_id = 1)
            int userId = 1;

            List<Order> userOrders = OrderMapper.getAllOrdersUser(connectionPool, userId);

            // Verify that the number of orders for the user matches the expected count
            int expected = 1; // Assuming there's only one order for user_id = 1 in the test data
            assertEquals(expected, userOrders.size());

        } catch (DatabaseException e) {
            fail("Database exception occurred: " + e.getMessage());
        }

    }

    @Test
    void insertOrder() {
        try {

            // Create a new Order object
            Order newOrder = new Order(1, 1, 550, 750, 1000, 750, 550);
            // Assuming the user ID is set correctly as 3 for the newly inserted user

            // Get the initial count of orders in the database
            Order insertedOrder = OrderMapper.insertOrder(newOrder, connectionPool); // Ensure unique ID for the test

            // Verify that the returned order object is not null
            assertEquals(newOrder, insertedOrder);

        } catch (DatabaseException e) {
            fail("Database exception occurred: " + e.getMessage());
        }
    }

}










