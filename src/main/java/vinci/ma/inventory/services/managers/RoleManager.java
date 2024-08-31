package vinci.ma.inventory.services.managers;

import vinci.ma.inventory.dao.entities.Role;

import java.util.List;

public interface RoleManager {

    Role createRole(Role role);
    Role getRoleById(Long id);
    List<Role> getAllRoles();
    void deleteRole(Long id);


}
