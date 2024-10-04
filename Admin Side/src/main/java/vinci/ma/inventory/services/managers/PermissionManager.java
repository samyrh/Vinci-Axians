package vinci.ma.inventory.services.managers;

import vinci.ma.inventory.dao.entities.Permission;

import java.util.List;

public interface PermissionManager {
    Permission createPermission(Permission permission);
    Permission getPermissionById(Long id);
    List<Permission> getAllPermissions();
    void deletePermission(Long id);
}
