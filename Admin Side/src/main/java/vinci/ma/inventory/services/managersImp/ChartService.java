package vinci.ma.inventory.services.managersImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vinci.ma.inventory.dao.repositories.CategoryRepo;
import vinci.ma.inventory.dao.repositories.DepartmentRepo;
import vinci.ma.inventory.dao.repositories.SupplierRepo;
import vinci.ma.inventory.dto.CategoryDtoChart;
import vinci.ma.inventory.dto.DepartmentDtoChart;
import vinci.ma.inventory.dto.SupplierDtoChart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChartService {

    @Autowired
    private CategoryRepo categoryRepository;

    @Autowired
    private SupplierRepo supplierRepository;

    @Autowired
    private DepartmentRepo departmentRepository;

    public List<Map<String, Object>> getCategoryData() {
        return categoryRepository.getCategoryData();
    }

    public List<Map<String, Object>> getSupplierData() {
        return supplierRepository.getSupplierData();
    }

    public List<Map<String, Object>> getDepartmentData() {
        return departmentRepository.getDepartmentData();
    }
}
