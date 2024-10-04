package vinci.ma.inventory.dao.enums;

import vinci.ma.inventory.dao.entities.Permission;

public enum PermissionType {
    READ("Read"),
    ASSIGN_ROLE("Assign Role"),
    MANAGE_MATERIAL("Manage Material"),
    MANAGE_USERS("Manage Users");



    private final String displayName;

    PermissionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }




}

