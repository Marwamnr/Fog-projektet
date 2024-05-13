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
    private static final int RAFTERS = 2; // Material IDs for rafters
    private static final int STRAPS = 2; // Material IDs for beams

    private List<OrderLine> orderLines;
    private int width;
    private int length;
    private ConnectionPool connectionPool;

    public Calculator(int width, int length, ConnectionPool connectionPool) {
        this.width = width;
        this.length = length;
        this.connectionPool = connectionPool;
        this.orderLines = new ArrayList<>(); // Initialize the list

    }

    public void calcCarport(Order order) throws DatabaseException {
        calcPosts(order);
        calcRafters(order);
        calcStraps(order);

    }

    // Stolper
    private void calcPosts(Order order) throws DatabaseException {
        int quantity = calcPostsQuantity();
        List<Material> materials = MaterialMapper.getMaterialsByWidthAndMinLength(0, POSTS, connectionPool);
        Material material = materials.get(0);
        OrderLine orderLine = new OrderLine(0, order.getOrderId(), material.getMaterialId(), "Stolper nedgraves 90 cm. i jord", quantity);
        orderLines.add(orderLine);
    }

    public int calcPostsQuantity() {

        return 2 * (2 + (length - 130) / 340);
    }

    // Spær
    private void calcRafters(Order order) throws DatabaseException {
        int quantity = calcRaftersQuantity();
        List<Material> materials = MaterialMapper.getMaterialsByWidthAndMinLength(0, RAFTERS, connectionPool);
        Material material = materials.get(0);
        OrderLine orderLine = new OrderLine(0, order.getOrderId(), material.getMaterialId(), "Spær, monteres på rem", quantity);
        orderLines.add(orderLine);
    }

    public int calcRaftersQuantity() {
        return (int) Math.round(1 +(length / 55.0));
    }

    // Remme
    private void calcStraps(Order order) throws DatabaseException {
        int quantity = calcStrapsQuantity();
            List<Material> materials = MaterialMapper.getMaterialsByWidthAndMinLength(0, STRAPS, connectionPool);
        Material material = materials.get(0);
        OrderLine orderLine = new OrderLine(0, order.getOrderId(), material.getMaterialId(), "Remme i sider, sadles ned i stolper", quantity);
        orderLines.add(orderLine);
    }


    public int calcStrapsQuantity() {

       return (int)Math.ceil(2.0 * length / 600);

    }


    public List<OrderLine> getOrderLines() {
        return orderLines;
    }
}
