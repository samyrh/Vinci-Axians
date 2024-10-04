package vinci.ma.inventory.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vinci.ma.inventory.dao.entities.Permission;
import vinci.ma.inventory.dao.enums.PermissionType;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, Long> {
    Permission findPermissionByName(PermissionType type);
}