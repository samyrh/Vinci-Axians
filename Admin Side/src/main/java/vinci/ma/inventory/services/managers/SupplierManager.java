package vinci.ma.inventory.services.managers;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import vinci.ma.inventory.dao.entities.Supplier;

import java.util.List;


public interface SupplierManager{

    Supplier createSupplier(Supplier supplier);
    Supplier getSupplierById(Long id);
    List<Supplier> getAllSuppliers();
    @Transactional
    void deleteSupplier(Long id);



    boolean isFullNameTaken(String fullName);
    boolean isPhoneTaken(String phone);
    boolean isSupplierCodeTaken(String supplierCode);
    boolean isContactEmailTaken(String contactEmail);
    void updateSupplier(Long supplierId, String fullName, String phone, String supplierCode, String contactEmail, String address, String description);

}
