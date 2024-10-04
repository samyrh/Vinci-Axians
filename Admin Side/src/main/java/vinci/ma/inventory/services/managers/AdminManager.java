package vinci.ma.inventory.services.managers;

import vinci.ma.inventory.dao.entities.Admin;
import vinci.ma.inventory.dao.entities.Material;

import java.util.List;

public interface AdminManager {

    Admin createAdmin(Admin admin);
    Admin getAdminById(Long id);
    List<Admin> getAllAdmins();
    void deleteAdmin(Long id);
    Admin assignMaterialToAdmin(Long adminId, Long materialId);
    List<Material> getMaterialsManagedByAdmin(Long adminId);
}
