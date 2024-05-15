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
        List<Material> materials = MaterialMapper.getMaterialsByMinLengthAndGroupId(0, POSTS, connectionPool);
        Material material = materials.get(0);
        OrderLine orderLine = new OrderLine(0, order.getOrderId(), material.getMaterialId(), "Stolper nedgraves 90 cm. i jord", quantity);
        orderLines.add(orderLine);
    }

    public int calcPostsQuantity() {

        return 2 * (2 + (length - 130) / 340);
    }

    // Spær
    private void calcRafters(Order order) throws DatabaseException {
        List<Material> materials = MaterialMapper.getMaterialsByMinLengthAndGroupId(length, RAFTERS, connectionPool);

        // If no materials of exact length are found, get the closest material with length greater than or equal to half of the carport length
        if (materials.isEmpty()) {
            // Calculate half of the carport length
            int halfCarportLength = length / 2;

            // Get materials with length greater than or equal to half of the carport length
            materials = MaterialMapper.getMaterialsByMinLengthAndGroupId(halfCarportLength, RAFTERS, connectionPool);
        }

        // If materials are still not found, handle the case
        if (materials.isEmpty()) {
            // Handle case when no suitable materials are found
            System.out.println("No suitable materials found for rafters.");
            return;
        }

        int quantity = calcRaftersQuantity();
        OrderLine orderLine = new OrderLine(0, order.getOrderId(), materials.get(0).getMaterialId(), "Spær, monteres på rem", quantity);
        orderLines.add(orderLine);

    }

    public int calcRaftersQuantity() {
        return (int) Math.ceil((double) length / 55);
    }

    private void calcStraps(Order order) throws DatabaseException {
        List<Material> materials = MaterialMapper.getMaterialsByMinLengthAndGroupId(length, STRAPS, connectionPool);

        // If no materials of exact length are found, get the closest material with length greater than or equal to half of the carport length
        if (materials.isEmpty()) {
            // Calculate half of the carport length
            int halfCarportLength = length / 2;

            // Get materials with length greater than or equal to half of the carport length
            materials = MaterialMapper.getMaterialsByMinLengthAndGroupId(halfCarportLength, STRAPS, connectionPool);
        }

        // If materials are still not found, handle the case
        if (materials.isEmpty()) {
            // Handle case when no suitable materials are found
            System.out.println("No suitable materials found for straps.");
            return;
        }

        int quantity = calcStrapsQuantity(materials.get(0).getLength());
        OrderLine orderLine = new OrderLine(0, order.getOrderId(), materials.get(0).getMaterialId(), "Remme i sider, sadles ned i stolper", quantity);
        orderLines.add(orderLine);
    }

    public int calcStrapsQuantity(int strapLength) {
        // Ensure there are at least 2 straps (one for each side)
        return (int) Math.ceil(2.0 * length / strapLength);
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }
}