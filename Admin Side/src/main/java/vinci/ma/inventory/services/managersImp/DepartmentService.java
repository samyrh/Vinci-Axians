package vinci.ma.inventory.services.managersImp;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vinci.ma.inventory.dao.entities.Department;
import vinci.ma.inventory.dao.repositories.AdminRepo;
import vinci.ma.inventory.dao.repositories.DepartmentRepo;
import vinci.ma.inventory.dao.repositories.RoleRepo;
import vinci.ma.inventory.dao.repositories.UserRepo;
import vinci.ma.inventory.services.managers.DepartmentManager;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService implements DepartmentManager
{


    @Autowired
    private DepartmentRepo departmentRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private RoleRepo roleRepo;



    @Override
    public List<Department> getAllDepartments() {

        return departmentRepo.findAll();
    }

    @Override
    public Department getDepartmentById(Long departmentId) {
        Optional<Department> departmentOptional = departmentRepo.findById(departmentId);
        if (departmentOptional.isPresent()) {
            return departmentOptional.get();
        } else {
            throw new EntityNotFoundException("Department with ID " + departmentId + " not found");
        }

    }


}
