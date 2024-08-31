package vinci.ma.inventory.services.managers;

import vinci.ma.inventory.dao.entities.User;
import vinci.ma.inventory.dao.enums.RoleType;

import java.util.List;

public interface UserManager {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    void deleteUser(Long id);
    User assignRoleToUser(String username, RoleType roleType);
    User updateUser(Long userId, User updatedUserDetails);
    boolean isEmailTaken(String email, Long userId);
    boolean isUsernameTaken(String username, Long userId);
    public boolean isPhoneTaken(String phone, Long userId);
}
