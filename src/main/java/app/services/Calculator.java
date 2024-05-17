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
    // group IDs
    private static final int POSTS = 1;
    private static final int RAFTERS = 2;
    private static final int STRAPS = 2;
    private static final int STERNS = 3;

    private List<OrderLine> orderLines;
    private int carportwidth;
    private int carportlength;
    private int toolroomwidth;
    private int toolroomlength;
    private ConnectionPool connectionPool;

    public Calculator(int carportwidth, int carportlength, int toolroomwidth, int toolroomlength, ConnectionPool connectionPool) {
        this.carportwidth = carportwidth;
        this.carportlength = carportlength;
        this.toolroomwidth = toolroomwidth;
        this.toolroomlength = toolroomlength;
        this.connectionPool = connectionPool;
        this.orderLines = new ArrayList<>(); // Initialize the list

    }

    public void calcCarport(Order order) throws DatabaseException {
        calcPosts(order);
        calcRafters(order);
        calcStrapsPort(order);
        calcStrapsShed(order);
        //calcSternFrontBack(order);
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

        return 2 * (2 + (carportlength - 130) / 340);
    }

    // Spær
    private void calcRafters(Order order) throws DatabaseException {
        int totalLength = carportlength + toolroomlength; // Calculate total length

        List<Material> materials = MaterialMapper.getMaterialsByMinLengthAndGroupId(totalLength, RAFTERS, connectionPool);

        // If no materials of exact length are found, get the closest material with length greater than or equal to half of the total length
        if (materials.isEmpty()) {
            // Calculate half of the total length
            int halfTotalLength = totalLength / 2;

            // Get materials with length greater than or equal to half of the total length
            materials = MaterialMapper.getMaterialsByMinLengthAndGroupId(halfTotalLength, RAFTERS, connectionPool);
        }

        // If materials are still not found, handle the case
        if (materials.isEmpty()) {
            // Handle case when no suitable materials are found
            System.out.println("No suitable materials found for rafters.");
            return;
        }

        int quantity = calcRaftersQuantity(totalLength); // Pass total length to the calculation method
        OrderLine orderLine = new OrderLine(0, order.getOrderId(), materials.get(0).getMaterialId(), "Spær, monteres på rem", quantity);
        orderLines.add(orderLine);
    }

    public int calcRaftersQuantity(int totalLength) {
        return (int) Math.ceil((double) totalLength / 55);
    }

    private void calcStrapsPort(Order order) throws DatabaseException {
        List<Material> materials = MaterialMapper.getMaterialsByMinLengthAndGroupId(carportlength, STRAPS, connectionPool);

        // If no materials of exact length are found, get the closest material with length greater than or equal to half of the carport length
        if (materials.isEmpty()) {
            // Calculate half of the carport length
            int halfCarportLength = carportlength / 2;

            // Get materials with length greater than or equal to half of the carport length
            materials = MaterialMapper.getMaterialsByMinLengthAndGroupId(halfCarportLength, STRAPS, connectionPool);
        }

        // If materials are still not found, handle the case
        if (materials.isEmpty()) {
            // Handle case when no suitable materials are found
            System.out.println("No suitable materials found for straps.");
            return;
        }

        int quantity = calcStrapsPortQuantity(materials.get(0).getLength());
        OrderLine orderLine = new OrderLine(0, order.getOrderId(), materials.get(0).getMaterialId(), "Remme i sider, sadles ned i stolper", quantity);
        orderLines.add(orderLine);
    }

    public int calcStrapsPortQuantity(int strapLength) {
        // Ensure there are at least 2 straps (one for each side)
        return (int) Math.ceil(2.0 * carportlength / strapLength);
    }

    private void calcStrapsShed(Order order) throws DatabaseException {
        List<Material> materials = MaterialMapper.getMaterialsByMinLengthAndGroupId(toolroomlength, STRAPS, connectionPool);

        // If no materials of exact length are found, get the closest material with length greater than or equal to half of the carport length
        if (materials.isEmpty()) {
            // Calculate half of the carport length
            int halfToolroomLength = toolroomlength / 2;

            // Get materials with length greater than or equal to half of the carport length
            materials = MaterialMapper.getMaterialsByMinLengthAndGroupId(halfToolroomLength, STRAPS, connectionPool);
        }

        // If materials are still not found, handle the case
        if (materials.isEmpty()) {
            // Handle case when no suitable materials are found
            System.out.println("No suitable materials found for straps.");
            return;
        }

        int quantity = calcStrapsShedQuantity(materials.get(0).getLength());
        OrderLine orderLine = new OrderLine(0, order.getOrderId(), materials.get(0).getMaterialId(), "Remme i sider, sadles ned i stolper" +
                "(skur del, deles)", quantity);
        orderLines.add(orderLine);
    }

    public int calcStrapsShedQuantity(int strapLength) {
        // Ensure there are at least 2 straps (one for each side)
        return (int) Math.ceil(2.0 * toolroomlength / strapLength);

    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }
}