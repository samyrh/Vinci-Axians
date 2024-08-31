package vinci.ma.inventory.services.managersImp;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vinci.ma.inventory.dao.entities.Department;
import vinci.ma.inventory.dao.entities.Role;
import vinci.ma.inventory.dao.entities.User;
import vinci.ma.inventory.dao.enums.RoleType;
import vinci.ma.inventory.dao.repositories.DepartmentRepo;
import vinci.ma.inventory.dao.repositories.NotificationRepo;
import vinci.ma.inventory.dao.repositories.RoleRepo;
import vinci.ma.inventory.dao.repositories.UserRepo;
import vinci.ma.inventory.services.managers.DepartmentManager;
import vinci.ma.inventory.services.managers.UserManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserManager {


    @Autowired
    private UserRepo userRepository;

    @Autowired
    private RoleRepo roleRepository;

    @Autowired
    private DepartmentRepo departmentRepository;
    @Autowired
    private DepartmentManager departmentManager;

    @Autowired
    private NotificationRepo notificationRepository;
    @Autowired
    private NotificationService notificationService;


    @Override
    public User createUser(User user) {
        // Check if user exists by username
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("User with username already exists.");
        }

        // Check if user exists by email
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("User with email already exists.");
        }

        // Check if user exists by phone number
        if (userRepository.findUserByPhone(user.getPhone()) != null) {
            throw new RuntimeException("User with phone number already exists.");
        }

        // Check if user exists by last name
        if (userRepository.findUserByLastName(user.getLastName()) != null) {
            throw new RuntimeException("User with last name already exists.");
        }

        // Get Role and Department
        Role role = roleRepository.findById(user.getRole().getId())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Department department = departmentRepository.findById(user.getDepartment().getId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // Set role and department
        user.setRole(role);
        user.setDepartment(department);

        // Save user
        userRepository.save(user);

        // Notify admins and the user
        notificationService.notifyUser(user);
        notificationService.notifyAdmins(user);

        return user;
    }


    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        User deletedUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete all notifications associated with the user
        notificationRepository.deleteByUserId(userId);

        // Delete the user
        userRepository.delete(deletedUser);

        // Notify all admins and users with the role "Admin" about the user deletion
        notificationService.notifyAdminsAndAdminUsersAboutDeletion(deletedUser);
    }

    @Override
    @Transactional
    public User assignRoleToUser(String username, RoleType roleType) {
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleType);
        if (user != null && role != null) {
            user.setRole(role);
            userRepository.save(user);
        }
        return user;
    }


    @Transactional
    @Override
    public User updateUser(Long userId, User updatedUserDetails) {
        // Find the existing user
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Boolean flag to check if any field is updated
        boolean isUpdated = false;
        StringBuilder editedFields = new StringBuilder();

        // Update fields and check if they have changed
        if (!existingUser.getUsername().equals(updatedUserDetails.getUsername())) {
            existingUser.setUsername(updatedUserDetails.getUsername());
            editedFields.append("Username, ");
            isUpdated = true;
        }

        if (!existingUser.getEmail().equals(updatedUserDetails.getEmail())) {
            existingUser.setEmail(updatedUserDetails.getEmail());
            editedFields.append("Email, ");
            isUpdated = true;
        }

        if (!existingUser.getPhone().equals(updatedUserDetails.getPhone())) {
            existingUser.setPhone(updatedUserDetails.getPhone());
            editedFields.append("Phone, ");
            isUpdated = true;
        }

        if (!existingUser.getFirstName().equals(updatedUserDetails.getFirstName())) {
            existingUser.setFirstName(updatedUserDetails.getFirstName());
            editedFields.append("First Name, ");
            isUpdated = true;
        }

        if (!existingUser.getLastName().equals(updatedUserDetails.getLastName())) {
            existingUser.setLastName(updatedUserDetails.getLastName());
            editedFields.append("Last Name, ");
            isUpdated = true;
        }

        if (!existingUser.getAddress().equals(updatedUserDetails.getAddress())) {
            existingUser.setAddress(updatedUserDetails.getAddress());
            editedFields.append("Address, ");
            isUpdated = true;
        }

        // Update role and department if they have changed
        if (!existingUser.getDepartment().getId().equals(updatedUserDetails.getDepartment().getId())) {
            Department newDepartment = departmentManager.getDepartmentById(updatedUserDetails.getDepartment().getId());
            if (newDepartment != null) {
                existingUser.setDepartment(newDepartment);
                editedFields.append("Department, ");
                isUpdated = true;
            }
        }

        if (!existingUser.getRole().getId().equals(updatedUserDetails.getRole().getId())) {
            Role newRole = roleRepository.findRoleById(updatedUserDetails.getRole().getId());
            if (newRole != null) {
                existingUser.setRole(newRole);
                editedFields.append("Role, ");
                isUpdated = true;
            }
        }


        // Remove trailing comma and space
        if (editedFields.length() > 0) {
            editedFields.setLength(editedFields.length() - 2);
        }

        // Save the updated user if any fields were changed
        if (isUpdated) {
            userRepository.save(existingUser);

            // Notify about the profile update
            notificationService.notifyProfileUpdate(existingUser);
        }

        return existingUser;
    }






    @Override
    public boolean isUsernameTaken(String username, Long userId) {
        User existingUser = userRepository.findByUsername(username);
        // Check if the username exists and belongs to a different user
        return existingUser != null && !existingUser.getId().equals(userId);
    }
    @Override
    public boolean isEmailTaken(String email, Long userId) {
        User existingUser = userRepository.findByEmail(email);
        return existingUser != null && !existingUser.getId().equals(userId);
    }

    @Override
    public boolean isPhoneTaken(String phone, Long userId) {
        User existingUser = userRepository.findByPhone(phone);
        return existingUser != null && !existingUser.getId().equals(userId);
    }

}

