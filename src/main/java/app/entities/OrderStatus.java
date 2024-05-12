package app.entities;

public class OrderStatus {
    private int orderStatusId;
    private String statusName;

    public OrderStatus(int orderStatusId, String statusName) {
        this.orderStatusId = orderStatusId;
        this.statusName = statusName;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "orderStatusId=" + orderStatusId +
                ", statusName='" + statusName + '\'' +
                '}';
    }
}
