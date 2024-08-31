package vinci.ma.inventory.dto;


public class DepartmentDTO {
    private Long id;
    private String name;
    private long totalUsers;
    private long totalAdmins;

    // Constructor
    public DepartmentDTO(Long id, String name, long totalUsers, long totalAdmins) {
        this.id = id;
        this.name = name;
        this.totalUsers = totalUsers;
        this.totalAdmins = totalAdmins;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalAdmins() {
        return totalAdmins;
    }

    public void setTotalAdmins(long totalAdmins) {
        this.totalAdmins = totalAdmins;
    }
}

