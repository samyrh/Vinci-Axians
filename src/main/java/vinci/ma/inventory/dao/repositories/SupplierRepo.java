package vinci.ma.inventory.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vinci.ma.inventory.dao.entities.Material;
import vinci.ma.inventory.dao.entities.Supplier;

import java.util.List;

@Repository
public interface SupplierRepo extends JpaRepository<Supplier, Long> {

    boolean existsByFullName(String fullName);
    boolean existsByPhone(String phone);
    boolean existsBySupplierCode(String supplierCode);
    boolean existsByContactEmail(String contactEmail);

    List<Supplier> findByFullNameContainingOrSupplierCodeContaining(String fullName, String supplierCode);
    // Custom query methods (if any)
}
