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

        // If no materials of exact length are found, get the closest material with length greater than or equal to the desired length
        if (materials.isEmpty()) {
            materials = MaterialMapper.getMaterialsByMinLengthAndGroupId(length, RAFTERS, connectionPool);
        }

        // If materials are still not found, handle the case
        if (materials.isEmpty()) {
            // Handle case when no suitable materials are found
            return;
        }

        // Calculate the number of full sections and the remaining length
        int fullSections = length / materials.get(0).getLength();
        int remainingLength = length % materials.get(0).getLength();

        // If there's any remaining length, add an extra section to ensure coverage
        if (remainingLength > 0) {
            fullSections++; // Add an extra section to cover the remaining length
        }

        // Add full sections to the order lines
        if (fullSections > 0) {
            int quantity = calcRaftersQuantity();
            OrderLine orderLine = new OrderLine(0, order.getOrderId(), materials.get(0).getMaterialId(), "Spær, monteres på rem", quantity * fullSections);
            orderLines.add(orderLine);
        }
    }

    public int calcRaftersQuantity() {
        int quantity = (length / 55);
        // The '%' operator calculates the remainder when 'length' is divided by 55.
        // If there's any remaining length, add an extra rafter
        if (length % 55 > 0) {
            quantity++; // Add an extra rafter to cover the remaining length
        }

        return quantity;
    }

    // Remme
    private void calcStraps(Order order) throws DatabaseException {
        List<Material> materials = MaterialMapper.getMaterialsByMinLengthAndGroupId(length, STRAPS, connectionPool);

        // If no materials of exact length are found, get the closest material with length greater than or equal to the desired length
        if (materials.isEmpty()) {
            materials = MaterialMapper.getMaterialsByMinLengthAndGroupId(length, STRAPS, connectionPool);
        }

        // If materials are still not found, handle the case
        if (materials.isEmpty()) {
            // Handle case when no suitable materials are found
            return;
        }

        // Calculate the number of full sections and the remaining length
        int fullSections = length / materials.get(0).getLength();
        int remainingLength = length % materials.get(0).getLength();

        // If there's any remaining length, add an extra section to ensure coverage
        if (remainingLength > 0) {
            fullSections++; // Add an extra section to cover the remaining length
        }

        // Add full sections to the order lines
        if (fullSections > 0) {
            int quantity = calcStrapsQuantity();
            OrderLine orderLine = new OrderLine(0, order.getOrderId(), materials.get(0).getMaterialId(), "Remme i sider, sadles ned i stolper", quantity * fullSections);
            orderLines.add(orderLine);
        }
    }

    public int calcStrapsQuantity() {
        // Using the highest length of my material for straps, for minimizing waste of many resources
        // Math.ceil() is used to round up the result of the division to the nearest integer
        return (int) Math.ceil(2.0 * length / 600);
    }


    public List<OrderLine> getOrderLines() {
        return orderLines;
    }
}
