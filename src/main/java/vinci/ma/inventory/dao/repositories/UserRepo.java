package vinci.ma.inventory.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vinci.ma.inventory.dao.entities.Department;
import vinci.ma.inventory.dao.entities.Role;
import vinci.ma.inventory.dao.entities.User;
import vinci.ma.inventory.dao.enums.RoleType;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);

    User findUserByPhone(String phone);

    User findUserByLastName(String lastname);

    User findUserById(Long id);

    List<User> findAllByRoleName(RoleType roleType);

    User findByPhone(String phone);

    Long countByDepartmentId(Long departmentId);


    List<User> findByUsernameContainingOrFirstNameContainingOrLastNameContaining(String username, String firstName, String lastName);
}