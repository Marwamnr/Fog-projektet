package app.services;

import app.entities.Order;
import app.entities.OrderLine;
import app.entities.Material;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;

import java.util.ArrayList;
import java.util.List;

public class Calculator {
    private static final int POSTS = 1;
    private static final int[] RAFTERS = {2, 3, 4, 5, 6, 7}; // Material IDs for rafters
    private static final int[] BEAMS = {2, 3, 4, 5, 6, 7}; // Material IDs for beams

    private List<OrderLine> orderLines;
    private int width;
    private int length;
    private ConnectionPool connectionPool;

    public Calculator(int width, int length, ConnectionPool connectionPool) {
        this.width = width;
        this.length = length;
        this.connectionPool = connectionPool;
    }

    public void calcCarport(Order order) throws DatabaseException {
        calcPost(order);
        calcBeams(order);
        calcRafters(order);
    }

    // Stolper
    private void calcPost(Order order) throws DatabaseException {
        int quantity = calcPostQuantity();
        List<Material> materials = MaterialMapper.getMaterialsByWidthAndMinLength(0, 0, POSTS, connectionPool);
        Material material = materials.get(0);
        OrderLine orderLine = new OrderLine(0, order.getOrderId(), material.getMaterialId(), "Stolper nedgraves 90 cm. i jord", quantity);
        orderLines.add(orderLine);
    }

    private int calcPostQuantity() {
        return 2 * (2 + (length - 130) / 340);
    }

    // Remme
    private void calcBeams(Order order) throws DatabaseException {

        }


    private int calcBeamsQuantity() {
        return 0;
    }

    // Spær
    private void calcRafters(Order order) throws DatabaseException {

    }

    private int calcRaftersQuantity() {
        return 0;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }
}