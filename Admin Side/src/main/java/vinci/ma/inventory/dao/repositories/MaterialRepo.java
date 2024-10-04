package vinci.ma.inventory.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vinci.ma.inventory.dao.entities.Material;
import vinci.ma.inventory.dto.MaterialSupplierDTO;

import java.util.List;


@Repository
public interface MaterialRepo extends JpaRepository<Material, Long> {
  /*  @Query("SELECT m FROM Material m LEFT JOIN FETCH m.suppliers s LEFT JOIN FETCH m.category c WHERE m.id = :id")
    Material findByIdWithSuppliersAndCategory(@Param("id") Long id); */


    @Query("SELECT m FROM Material m " +
            "LEFT JOIN FETCH m.suppliers s " +
            "LEFT JOIN FETCH m.category c " +
            "WHERE m.id = :id")
    Material findByIdWithSuppliersAndCategory(@Param("id") Long id);

    @Query("SELECT m FROM Material m JOIN m.suppliers s WHERE s.id = :supplierId")
    List<Material> findBySupplierId(@Param("supplierId") Long supplierId);


    List<Material> findMaterialsByCategoryId(Long id);


    List<Material> findByNameContainingOrRefContaining(String name, String ref);
}