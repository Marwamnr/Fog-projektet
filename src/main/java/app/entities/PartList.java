package app.entities;

public class PartList {

    private String materialDescription;
    private int length;
    private int quantity;
    private String unit;
    private String orderLineDescription;

    public PartList(String materialDescription, int length, int quantity, String unit, String orderLineDescription) {
        this.materialDescription = materialDescription;
        this.length = length;
        this.quantity = quantity;
        this.unit = unit;
        this.orderLineDescription = orderLineDescription;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOrderLineDescription() {
        return orderLineDescription;
    }

    public void setOrderLineDescription(String orderLineDescription) {
        this.orderLineDescription = orderLineDescription;
    }

}
