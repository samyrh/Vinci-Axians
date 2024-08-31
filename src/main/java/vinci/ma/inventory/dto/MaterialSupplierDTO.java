package vinci.ma.inventory.dto;


public class MaterialSupplierDTO {
    private Long materialId;

    private String materialName;
    private String supplierCode;
    private String supplierName;
    private String description;
    private Double price;
    private Integer stock;
    private String dateEntered;
    private String categoryName;
    private double total;
    private String pictureBase64;
    private String ref;

    public MaterialSupplierDTO(Long materialId, String materialName, String supplierCode, String supplierName, String description, Double price, Integer stock, String dateEntered, String categoryName, double total, String pictureBase64, String ref) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.dateEntered = dateEntered;
        this.categoryName = categoryName;
        this.total = total;
        this.pictureBase64 = pictureBase64;
        this.ref = ref;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setDateEntered(String dateEntered) {
        this.dateEntered = dateEntered;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setPictureBase64(String pictureBase64) {
        this.pictureBase64 = pictureBase64;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Long getMaterialId() {
        return materialId;
    }


    public String getMaterialName() {
        return materialName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public String getDateEntered() {
        return dateEntered;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getTotal() {
        return total;
    }

    public String getPictureBase64() {
        return pictureBase64;
    }

    public String getRef() {
        return ref;
    }
}