package vinci.ma.inventory.services.managersImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vinci.ma.inventory.dao.entities.Role;
import vinci.ma.inventory.dao.repositories.RoleRepo;
import vinci.ma.inventory.services.managers.RoleManager;
import java.util.List;

@Service
public class RoleService implements RoleManager {


    @Autowired
    private RoleRepo roleRepository;

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
