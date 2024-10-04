package vinci.ma.inventory.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vinci.ma.inventory.dao.entities.Material;
import vinci.ma.inventory.dao.entities.Supplier;

import java.util.List;
import java.util.Map;

@Repository
public interface SupplierRepo extends JpaRepository<Supplier, Long> {

    boolean existsByFullName(String fullName);
    boolean existsByPhone(String phone);
    boolean existsBySupplierCode(String supplierCode);
    boolean existsByContactEmail(String contactEmail);

    // SupplierRepository
    @Query("SELECT new map(s.fullName as name, COUNT(m) as materialCount) FROM Supplier s LEFT JOIN s.materials m GROUP BY s.fullName")
    List<Map<String, Object>> getSupplierData();

    List<Supplier> findByFullNameContainingOrSupplierCodeContaining(String fullName, String supplierCode);
    // Custom query methods (if any)
}
