package vinci.ma.inventory.services.managers;

import vinci.ma.inventory.dao.entities.Material;
import vinci.ma.inventory.dto.MaterialSupplierDTO;

import java.util.List;

public interface MaterialManager {

    Material createMaterial(Material material);
    Material getMaterialById(Long id);
    List<Material> getAllMaterials();
    void deleteMaterial(Long id);


    List<MaterialSupplierDTO> getAllMaterialSupplierData();


    MaterialSupplierDTO getMaterialSupplierDetails(Long id);
}
