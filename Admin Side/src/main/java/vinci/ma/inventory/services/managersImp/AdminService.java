package vinci.ma.inventory.services.managersImp;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vinci.ma.inventory.dao.entities.Admin;
import vinci.ma.inventory.dao.entities.Material;
import vinci.ma.inventory.dao.repositories.AdminRepo;
import vinci.ma.inventory.dao.repositories.MaterialRepo;
import vinci.ma.inventory.services.managers.AdminManager;

import java.util.List;

@Service
public class AdminService implements AdminManager {
    @Autowired
    private AdminRepo adminRepository;

    @Autowired
    private MaterialRepo materialRepository;

    @Override
    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Admin assignMaterialToAdmin(Long adminId, Long materialId) {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        Material material = materialRepository.findById(materialId).orElse(null);
        if (admin != null && material != null) {
            admin.getMaterials().add(material);
            adminRepository.save(admin);
        }
        return admin;
    }

    @Override
    public List<Material> getMaterialsManagedByAdmin(Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        return admin != null ? admin.getMaterials() : null;
    }
}
