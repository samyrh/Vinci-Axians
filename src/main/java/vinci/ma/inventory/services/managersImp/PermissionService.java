package vinci.ma.inventory.services.managersImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vinci.ma.inventory.dao.entities.Permission;
import vinci.ma.inventory.dao.repositories.PermissionRepo;
import vinci.ma.inventory.services.managers.PermissionManager;

import java.util.List;

@Service
public class PermissionService implements PermissionManager {



    @Autowired
    private PermissionRepo permissionRepository;

    @Override
    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }

}
