package vinci.ma.inventory.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vinci.ma.inventory.dao.entities.Department;

import java.util.List;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Long> {
    // Custom query methods (if any)
    List<Department> findByNameContaining(String name);
}