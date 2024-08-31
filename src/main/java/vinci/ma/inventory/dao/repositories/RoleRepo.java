package vinci.ma.inventory.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vinci.ma.inventory.dao.entities.Role;
import vinci.ma.inventory.dao.enums.RoleType;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(RoleType name);
    Role findRoleById(Long id);
}