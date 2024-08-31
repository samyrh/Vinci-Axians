package vinci.ma.inventory.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vinci.ma.inventory.dao.entities.Admin;

import java.util.Optional;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {


    Long countByDepartmentId(Long departmentId);
    Optional<Admin> findAdminById(Long id);

    boolean existsAdminByUsername(String username);

    boolean existsAdminByEmail(String email);

    boolean existsAdminByPhone(String phone);

    Admin findAdminByUsername(String username);
}