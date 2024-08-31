package vinci.ma.inventory.services.managersImp;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vinci.ma.inventory.dao.entities.Admin;
import vinci.ma.inventory.dao.entities.Material;
import vinci.ma.inventory.dao.entities.Supplier;
import vinci.ma.inventory.dao.entities.User;
import vinci.ma.inventory.dao.repositories.AdminRepo;
import vinci.ma.inventory.dao.repositories.MaterialRepo;
import vinci.ma.inventory.dao.repositories.SupplierRepo;
import vinci.ma.inventory.dao.repositories.UserRepo;
import vinci.ma.inventory.dto.MaterialSupplierDTO;
import vinci.ma.inventory.services.managers.MaterialManager;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialService implements MaterialManager {


    @Autowired
    private MaterialRepo materialRepository;
    @Autowired
    private SupplierRepo supplierRepository;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private UserRepo userRepo;

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    @Override
    public Material createMaterial(Material material) {
        return materialRepository.save(material);
    }

    @Override
    public Material getMaterialById(Long id) {
        return materialRepository.findById(id).orElse(null);
    }

    @Override
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteMaterial(Long id) {

        // Find the material
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material not found"));

        // Remove the material from suppliers
        for (Supplier supplier : material.getSuppliers()) {
            supplier.getMaterials().remove(material);
            supplierRepository.save(supplier); // Consider using batch updates for efficiency
        }

        // Remove the material from admins
        for (Admin admin : material.getAdmins()) {
            admin.getMaterials().remove(material);
            adminRepo.save(admin); // Consider using batch updates for efficiency
        }

        // Remove the material from users
        for (User user : material.getUsers()) {
            user.getMaterials().remove(material);
            userRepo.save(user); // Consider using batch updates for efficiency
        }

        // Now delete the material
        materialRepository.deleteById(id);
    }

    @Override
    public List<MaterialSupplierDTO> getAllMaterialSupplierData() {
        List<Material> materials = materialRepository.findAll(); // Fetch materials

        return materials.stream()
                .flatMap(material -> material.getSuppliers().stream()
                        .map(supplier -> {
                            double price = material.getPrice();
                            int stock = material.getStock();
                            double total = price * stock; // Calculate total

                            // Convert byte[] to Base64 string
                            String pictureBase64 = material.getImage() != null
                                    ? Base64.getEncoder().encodeToString(material.getImage())
                                    : "";

                            // Return a new MaterialSupplierDTO with the correct values
                            return new MaterialSupplierDTO(
                                    material.getId(),
                                    material.getName(),
                                    supplier.getSupplierCode(),
                                    supplier.getFullName(),
                                    material.getDescription(), // Include the description
                                    price,
                                    stock,
                                    formatDate(material.getDateEntered()),
                                    material.getCategory() != null ? material.getCategory().getName() : "N/A",
                                    total,
                                    pictureBase64,
                                    material.getRef()
                            );
                        })
                ).collect(Collectors.toList());
    }

    // Helper method to format date
    private String formatDate(Date date) {
        if (date == null) {
            return "N/A";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .format(formatter);
    }


    @Override
    public MaterialSupplierDTO getMaterialSupplierDetails(Long id) {
        Material material = materialRepository.findByIdWithSuppliersAndCategory(id);

        if (material == null) {
            throw new EntityNotFoundException("Material not found with id " + id);
        }

        // Convert byte[] to Base64 string
        String pictureBase64 = material.getImage() != null ? Base64.getEncoder().encodeToString(material.getImage()) : "";

        return new MaterialSupplierDTO(
                material.getId(),
                material.getName(),
                material.getSuppliers().isEmpty() ? "N/A" : material.getSuppliers().get(0).getSupplierCode(),
                material.getSuppliers().isEmpty() ? "N/A" : material.getSuppliers().get(0).getFullName(),
                material.getDescription(),
                material.getPrice(),
                material.getStock(),
                material.getDateEntered() != null ? DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm").format(material.getDateEntered().toInstant().atZone(ZoneId.systemDefault())) : "N/A",
                material.getCategory() != null ? material.getCategory().getName() : "N/A",
                material.getPrice() * material.getStock(),
                pictureBase64,
                material.getRef()
        );
    }

}
