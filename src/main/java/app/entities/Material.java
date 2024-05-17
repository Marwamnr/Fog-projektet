package app.entities;

public class Material {
    private int materialId;
    private int width;
    private int length;
    private int meterPrice;
    private String description;
    private String unit;
    private int groupId;

    public Material(int materialId, int width, int length, int meterPrice, String description, String unit, int groupId) {
        this.materialId = materialId;
        this.width = width;
        this.length = length;
        this.meterPrice = meterPrice;
        this.description = description;
        this.unit = unit;
        this.groupId = groupId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getMeterPrice() {
        return meterPrice;
    }

    public void setMeterPrice(int meterPrice) {
        this.meterPrice = meterPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "Material{" +
                "materialId=" + materialId +
                ", width=" + width +
                ", length=" + length +
                ", meterPrice=" + meterPrice +
                ", description='" + description + '\'' +
                ", unit='" + unit + '\'' +
                ", groupId=" + groupId +
                '}';
    }
}


