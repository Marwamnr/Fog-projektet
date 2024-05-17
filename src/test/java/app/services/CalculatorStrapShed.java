package app.services;

import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorStrapShed {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "fog";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    @BeforeAll
    static void setup()
    {

    }
    @Test
    void calcStrapsShedQuantity() {
        Calculator calculator = new Calculator(0, 0,400,400, connectionPool);
        assertEquals(2, calculator.calcStrapsShedQuantity(420));
    }
}