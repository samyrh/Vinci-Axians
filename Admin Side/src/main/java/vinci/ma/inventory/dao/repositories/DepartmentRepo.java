package vinci.ma.inventory.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vinci.ma.inventory.dao.entities.Department;

import java.util.List;
import java.util.Map;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Long> {
    // Custom query methods (if any)


    // DepartmentRepository
    @Query("SELECT new map(d.name as name, COUNT(u) as userCount) FROM Department d LEFT JOIN d.users u GROUP BY d.name")
    List<Map<String, Object>> getDepartmentData();

    List<Department> findByNameContaining(String name);
}