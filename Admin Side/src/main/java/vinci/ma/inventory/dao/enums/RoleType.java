package vinci.ma.inventory.dao.enums;

public enum RoleType {
    ADMIN("Admin"),
    USER("User"),
    MANAGER("Manager"),
    GUEST("Guest");

    private final String displayName;

    RoleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}