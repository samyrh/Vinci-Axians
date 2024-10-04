package vinci.ma.inventory.services.managersImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vinci.ma.inventory.dao.entities.*;
import vinci.ma.inventory.dao.repositories.*;
import vinci.ma.inventory.services.managers.SearchManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService implements SearchManager {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private MaterialRepo materialRepository;

    @Autowired
    private SupplierRepo supplierRepository;

    @Autowired
    private CategoryRepo categoryRepository;

    @Autowired
    private DepartmentRepo departmentRepository;


    @Override
    public Map<String, List<?>> search(String query) {
        Map<String, List<?>> results = new HashMap<>();

        List<User> users = userRepository.findByUsernameContainingOrFirstNameContainingOrLastNameContaining(query, query, query);
        List<Material> materials = materialRepository.findByNameContainingOrRefContaining(query, query);
        List<Supplier> suppliers = supplierRepository.findByFullNameContainingOrSupplierCodeContaining(query, query);
        List<Category> categories = categoryRepository.findByNameContaining(query);
        List<Department> departments = departmentRepository.findByNameContaining(query);

        results.put("users", users);
        results.put("materials", materials);
        results.put("suppliers", suppliers);
        results.put("categories", categories);
        results.put("departments", departments);

        return results;
    }
}