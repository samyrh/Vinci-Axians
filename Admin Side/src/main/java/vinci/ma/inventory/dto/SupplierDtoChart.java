package vinci.ma.inventory.dto;
public class SupplierDtoChart {
    private String name;
    private int materialCount;

    // Constructors
    public SupplierDtoChart(String name, int materialCount) {
        this.name = name;
        this.materialCount = materialCount;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(int materialCount) {
        this.materialCount = materialCount;
    }
}