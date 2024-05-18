package app.entities;

import java.util.Objects;

public class Order {
    private int orderId;
    private int orderStatusId;
    private int userId;
    private int toolroomWidth;
    private int toolroomLength;
    private int totalPrice;
    private int carportWidth;
    private int carportLength;

    public Order(int orderId, int orderStatusId, int userId, int toolroomWidth, int toolroomLength, int totalPrice, int carportWidth, int carportLength) {
        this.orderId = orderId;
        this.orderStatusId = orderStatusId;
        this.userId = userId;
        this.toolroomWidth = toolroomWidth;
        this.toolroomLength = toolroomLength;
        this.totalPrice = totalPrice;
        this.carportWidth = carportWidth;
        this.carportLength = carportLength;
    }

    // for integration test
    public Order(int orderStatusId, int userId, int toolroomWidth, int toolroomLength, int totalPrice, int carportWidth, int carportLength) {
        this.orderStatusId = orderStatusId;
        this.userId = userId;
        this.toolroomWidth = toolroomWidth;
        this.toolroomLength = toolroomLength;
        this.totalPrice = totalPrice;
        this.carportWidth = carportWidth;
        this.carportLength = carportLength;
    }

    // Getters and Setters

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getToolroomWidth() {
        return toolroomWidth;
    }

    public void setToolroomWidth(int toolroomWidth) {
        this.toolroomWidth = toolroomWidth;
    }

    public int getToolroomLength() {
        return toolroomLength;
    }

    public void setToolroomLength(int toolroomLength) {
        this.toolroomLength = toolroomLength;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCarportWidth() {
        return carportWidth;
    }

    public void setCarportWidth(int carportWidth) {
        this.carportWidth = carportWidth;
    }

    public int getCarportLength() {
        return carportLength;
    }

    public void setCarportLength(int carportLength) {
        this.carportLength = carportLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (orderId != order.orderId) return false;
        if (orderStatusId != order.orderStatusId) return false;
        if (userId != order.userId) return false;
        if (toolroomWidth != order.toolroomWidth) return false;
        if (toolroomLength != order.toolroomLength) return false;
        if (totalPrice != order.totalPrice) return false;
        if (carportWidth != order.carportWidth) return false;
        return carportLength == order.carportLength;
    }

    @Override
    public int hashCode() {
        int result = orderId;
        result = 31 * result + orderStatusId;
        result = 31 * result + userId;
        result = 31 * result + toolroomWidth;
        result = 31 * result + toolroomLength;
        result = 31 * result + totalPrice;
        result = 31 * result + carportWidth;
        result = 31 * result + carportLength;
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderStatusId=" + orderStatusId +
                ", userId=" + userId +
                ", toolroomWidth=" + toolroomWidth +
                ", toolroomLength=" + toolroomLength +
                ", totalPrice=" + totalPrice +
                ", carportWidth=" + carportWidth +
                ", carportLength=" + carportLength +
                '}';
    }
}