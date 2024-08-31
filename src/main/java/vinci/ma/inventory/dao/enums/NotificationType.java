package vinci.ma.inventory.dao.enums;

public enum NotificationType {
    INFO("Information"),
    WARNING("Warning"),
    ERROR("Error"),
    EMAIL("Email");
    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}