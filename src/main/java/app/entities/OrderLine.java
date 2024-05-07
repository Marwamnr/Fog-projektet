package app.entities;

public class OrderLine {
    private int orderLineId;
    private int orderId;
    private int materialId;
    private String description;
    private int quantity;

    public OrderLine(int orderLineId, int orderId, int materialId, String description, int quantity) {
        this.orderLineId = orderLineId;
        this.orderId = orderId;
        this.materialId = materialId;
        this.description = description;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getOrderLineId() {
        return orderLineId;
    }

    public void setOrderLineId(int orderLineId) {
        this.orderLineId = orderLineId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // ToString method
    @Override
    public String toString() {
        return "OrderLine{" +
                "orderLineId=" + orderLineId +
                ", orderId=" + orderId +
                ", materialId=" + materialId +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}


