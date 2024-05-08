package app.entities;

public class Order {
    private int orderId;
    private int orderStatusId;
    private int userId;
    private int toolroomWidth;
    private int toolroomLength;
    private int totalPrice;
    private int carportWidth;
    private int carportLength;

    // Constructor
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

    // ToString method
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