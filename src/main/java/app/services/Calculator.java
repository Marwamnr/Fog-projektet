package app.services;

import app.entities.Material;
import app.entities.OrderLine;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;

import java.util.ArrayList;
import java.util.List;


public class Calculator {

    private ConnectionPool connectionPool;

    public Calculator(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<OrderLine> calcCarport(int width, int length) throws DatabaseException {
        List<OrderLine> orderItems = new ArrayList<>();

        try {
            // Calculate posts
            List<OrderLine> postOrderLines = calcPosts(length);
            orderItems.addAll(postOrderLines);

            // Calculate beams
            List<OrderLine> beamOrderLines = calcBeams(width, length);
            orderItems.addAll(beamOrderLines);

            // Calculate rafters
            List<OrderLine> rafterOrderLines = calcRafters(length);
            orderItems.addAll(rafterOrderLines);

        } catch (DatabaseException e) {
            // Handle database exception
            throw new DatabaseException("Error occurred while calculating carport components: " + e.getMessage());
        }

        return orderItems;
    }

    // Stolper
    private List<OrderLine> calcPosts(int length) throws DatabaseException {
        List<OrderLine> postOrderLines = new ArrayList<>();
        List<Material> postMaterials = MaterialMapper.getMaterialsByWidthAndMinLength(length, 1, connectionPool);
        for (Material material : postMaterials) {
            int postQuantity = calcPostQuantity(length);
            OrderLine postOrderLine = new OrderLine(1, 1, material.getMaterialId(), material.getDescription(), postQuantity);
            postOrderLines.add(postOrderLine);
        }
        return postOrderLines;
    }

    // Remme
    // Remme
    private List<OrderLine> calcBeams(int width, int length) throws DatabaseException {
        List<OrderLine> beamOrderLines = new ArrayList<>();
        List<Material> beamMaterials = MaterialMapper.getMaterialsByWidthAndMinLength(width, 2, connectionPool);
        for (Material material : beamMaterials) {
            int beamQuantity = width / material.getWidth(); // Assuming material width corresponds to beam width
            OrderLine beamOrderLine = new OrderLine(2, 1, material.getMaterialId(), material.getDescription(), beamQuantity);
            beamOrderLines.add(beamOrderLine);
        }
        return beamOrderLines;
    }


    // Spær
    private List<OrderLine> calcRafters(int length) throws DatabaseException {
        List<OrderLine> rafterOrderLines = new ArrayList<>();
        List<Material> rafterMaterials = MaterialMapper.getMaterialsByWidthAndMinLength(length, 2, connectionPool);
        for (Material material : rafterMaterials) {
            int rafterQuantity = length / material.getLength(); // Assuming material length corresponds to rafter length
            OrderLine rafterOrderLine = new OrderLine(3, 1, material.getMaterialId(), material.getDescription(), rafterQuantity);
            rafterOrderLines.add(rafterOrderLine);
        }
        return rafterOrderLines;
    }

    // Calculate post quantity based on carport length
    private int calcPostQuantity(int length) {
        return 2 * (2 + (length - 130) / 340);
    }
}

