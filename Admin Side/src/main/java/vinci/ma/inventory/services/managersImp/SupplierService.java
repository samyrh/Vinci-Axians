package vinci.ma.inventory.services.managersImp;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vinci.ma.inventory.dao.entities.Material;
import vinci.ma.inventory.dao.entities.Supplier;
import vinci.ma.inventory.dao.repositories.MaterialRepo;
import vinci.ma.inventory.dao.repositories.SupplierRepo;
import vinci.ma.inventory.services.managers.SupplierManager;

import java.util.List;

@Service
public class SupplierService implements SupplierManager {



    @Autowired
    private SupplierRepo supplierRepository;
    @Autowired
    private MaterialRepo materialRepo;

    @Override
    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id).orElse(null);
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        // Find the supplier
        Supplier supplier = supplierRepository.findById(id).orElse(null);
        if (supplier == null) {
            throw new EntityNotFoundException("Supplier not found");
        }

        // Remove supplier reference from associated materials
        for (Material material : supplier.getMaterials()) {
            material.getSuppliers().remove(supplier); // Remove the supplier reference
            materialRepo.save(material); // Save changes using materialRepo
        }

        // Now delete the supplier
        supplierRepository.deleteById(id);
    }
    // Check if full name is already taken
    @Override
    public boolean isFullNameTaken(String fullName) {
        return supplierRepository.existsByFullName(fullName);
    }

    // Check if phone number is already taken
    @Override
    public boolean isPhoneTaken(String phone) {
        return supplierRepository.existsByPhone(phone);
    }

    // Check if supplier code is already taken
    @Override
    public boolean isSupplierCodeTaken(String supplierCode) {
        return supplierRepository.existsBySupplierCode(supplierCode);
    }

    // Check if contact email is already taken
    @Override
    public boolean isContactEmailTaken(String contactEmail) {
        return supplierRepository.existsByContactEmail(contactEmail);
    }

    @Override
    public void updateSupplier(Long supplierId, String fullName, String phone, String supplierCode, String contactEmail, String address, String description) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplier.setFullName(fullName);
        supplier.setPhone(phone);
        supplier.setSupplierCode(supplierCode);
        supplier.setContactEmail(contactEmail);
        supplier.setAddress(address);
        supplier.setDescription(description);

        supplierRepository.save(supplier);
    }

}
