package vinci.ma.inventory.dto;
public class DepartmentDtoChart {
    private String name;
    private int userCount;

    // Constructors
    public DepartmentDtoChart(String name, int userCount) {
        this.name = name;
        this.userCount = userCount;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}
