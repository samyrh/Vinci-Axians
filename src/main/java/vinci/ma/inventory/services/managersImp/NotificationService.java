package vinci.ma.inventory.services.managersImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vinci.ma.inventory.dao.entities.Admin;
import vinci.ma.inventory.dao.entities.Notification;
import vinci.ma.inventory.dao.entities.User;
import vinci.ma.inventory.dao.enums.NotificationType;
import vinci.ma.inventory.dao.enums.RoleType;
import vinci.ma.inventory.dao.repositories.AdminRepo;
import vinci.ma.inventory.dao.repositories.NotificationRepo;
import vinci.ma.inventory.dao.repositories.UserRepo;
import vinci.ma.inventory.services.managers.NotificationManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class NotificationService implements NotificationManager {

    @Autowired
    private AdminRepo adminRepository;
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private NotificationRepo notificationRepository;

    @Override
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public void sendNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            // Logic to send notification (e.g., email)
        }
    }

    public void notifyUser(User user) { // notification about adding a user by admin to user himself
        Notification notification = new Notification();
        notification.setMessage("Welcome, " + user.getUsername() + "! Your account has been created.");
        notification.setDate(new Date());
        notification.setType(NotificationType.INFO);
        notification.setUser(user);
        notificationRepository.save(notification);
    }

    public void notifyAdmins(User user) {
        // Notify all admins about the new user
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            Notification notification = new Notification();
            notification.setMessage("New user created: " + user.getUsername());
            notification.setDate(new Date());
            notification.setType(NotificationType.INFO);
            notification.setAdmin(admin);
            notificationRepository.save(notification); // Save the notification
        }

        // Notify all users with the role "Admin" about the new user
        List<User> adminUsers = userRepository.findAllByRoleName(RoleType.ADMIN);
        for (User adminUser : adminUsers) {
            Notification notification = new Notification();
            notification.setMessage("New user created: " + user.getUsername());
            notification.setDate(new Date());
            notification.setType(NotificationType.INFO);
            notification.setUser(adminUser); // Set the user for this notification
            notificationRepository.save(notification); // Save the notification
        }
    }


    public void notifyAdminsAndAdminUsersAboutDeletion(User deletedUser) { // notify all admins and users with role admin about dleting an user

        // Notify all admins about the user deletion
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            Notification notification = new Notification();
            notification.setMessage("User deleted: " + deletedUser.getUsername());
            notification.setDate(new Date());
            notification.setType(NotificationType.WARNING);
            notification.setAdmin(admin);
            notificationRepository.save(notification); // Save the notification
        }

        // Notify all users with the role "Admin" about the user deletion
        List<User> adminUsers = userRepository.findAllByRoleName(RoleType.ADMIN);
        for (User adminUser : adminUsers) {
            Notification notification = new Notification();
            notification.setMessage("User deleted: " + deletedUser.getUsername());
            notification.setDate(new Date());
            notification.setType(NotificationType.WARNING);
            notification.setUser(adminUser); // Set the user for this notification
            notificationRepository.save(notification); // Save the notification
        }
    }


    @Override
    public void notifyProfileUpdate(User updatedUser) {
        String message = String.format(
                "User profile updated: Username: %s, Department: %s, Update Time: %s",
                updatedUser.getUsername(),
                updatedUser.getDepartment() != null ? updatedUser.getDepartment().getName() : "N/A",
                new Date().toString()
        );

        // Notify the user about the profile update
        Notification userNotification = new Notification();
        userNotification.setMessage(message);
        userNotification.setDate(new Date());
        userNotification.setType(NotificationType.INFO);
        userNotification.setUser(updatedUser);
        notificationRepository.save(userNotification);

        // Notify all admins about the profile update
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            Notification adminNotification = new Notification();
            adminNotification.setMessage(message);
            adminNotification.setDate(new Date());
            adminNotification.setType(NotificationType.INFO);
            adminNotification.setAdmin(admin);
            notificationRepository.save(adminNotification);
        }

        // Notify all users with the role "Admin" about the profile update
        List<User> adminUsers = userRepository.findAllByRoleName(RoleType.ADMIN);
        for (User adminUser : adminUsers) {
            Notification adminUserNotification = new Notification();
            adminUserNotification.setMessage(message);
            adminUserNotification.setDate(new Date());
            adminUserNotification.setType(NotificationType.INFO);
            adminUserNotification.setUser(adminUser);
            notificationRepository.save(adminUserNotification);
        }
    }







    @Override
    public void notifyAdminsAndAdminUsersAboutDepartmentDeletion(String departmentName) {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date()); // Format the current date and time

        // Notify all admins about the department deletion
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            Notification notification = new Notification();
            notification.setMessage("Department deleted: " + departmentName + " on " + formattedDate);
            notification.setDate(new Date());
            notification.setType(NotificationType.WARNING);
            notification.setAdmin(admin);
            notificationRepository.save(notification); // Save the notification
        }

        // Notify all users with the role "Admin" about the department deletion
        List<User> adminUsers = userRepository.findAllByRoleName(RoleType.ADMIN);
        for (User adminUser : adminUsers) {
            Notification notification = new Notification();
            notification.setMessage("Department deleted: " + departmentName + " on " + formattedDate);
            notification.setDate(new Date());
            notification.setType(NotificationType.WARNING);
            notification.setUser(adminUser); // Set the user for this notification
            notificationRepository.save(notification); // Save the notification
        }
    }




    @Override
    public void notifyAdminsAndAdminUsersAboutDepartmentCreation(String departmentName, String timestamp) {
        String message = "Department created: " + departmentName + " at " + timestamp;

        // Notify all admins
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.INFO);
            notification.setAdmin(admin);
            notificationRepository.save(notification);
        }

        // Notify all users with the role "Admin"
        List<User> adminUsers = userRepository.findAllByRoleName(RoleType.ADMIN);
        for (User adminUser : adminUsers) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.INFO);
            notification.setUser(adminUser);
            notificationRepository.save(notification);
        }
    }

    @Override
    public void notifyAdminsAndAdminUsersAboutCategoryCreation(String categoryName, String timestamp) {
        String message = "A new category added: " + categoryName + " on " + timestamp;

        // Notify all admins
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.INFO);
            notification.setAdmin(admin);
            notificationRepository.save(notification);
        }

        // Notify all users with the role "Admin"
        List<User> adminUsers = userRepository.findAllByRoleName(RoleType.ADMIN);
        for (User adminUser : adminUsers) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.INFO);
            notification.setUser(adminUser);
            notificationRepository.save(notification);
        }
    }








    @Override
    public void notifyAdminsAndAdminUsersAboutCategoryDeletion(String categoryName, String timestamp) {  // when i delete a category categories page
        String message = "Category deleted: " + categoryName + " at " + timestamp;

        // Notify all admins
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.WARNING);
            notification.setAdmin(admin);
            notificationRepository.save(notification);
        }

        // Notify all users with the role "ADMIN"
        List<User> adminUsers = userRepository.findAllByRoleName(RoleType.ADMIN);
        for (User adminUser : adminUsers) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.WARNING);
            notification.setUser(adminUser);
            notificationRepository.save(notification);
        }
    }



    public void notifyAdminsAndAdminUsersAboutCategoryUpdate(String categoryName, String timestamp) { // Notify Admins And Users with role Admin about category update
        String message = "Category updated: " + categoryName + " at " + timestamp;

        // Notify all admins
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.INFO);
            notification.setAdmin(admin);
            notificationRepository.save(notification);
        }

        // Notify all users with the role "ADMIN"
        List<User> adminUsers = userRepository.findAllByRoleName(RoleType.ADMIN);
        for (User adminUser : adminUsers) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.INFO);
            notification.setUser(adminUser);
            notificationRepository.save(notification);
        }
    }




    @Override
    public void notifyAdminsAndAdminUsersAboutSupplierAddition(String supplierName, String timestamp) { // about adding anew supplier
        String message = "New supplier added: " + supplierName + " at " + timestamp;

        // Notify all admins
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.INFO);
            notification.setAdmin(admin);
            notificationRepository.save(notification);
        }

        // Notify all users with the role "ADMIN"
        List<User> adminUsers = userRepository.findAllByRoleName(RoleType.ADMIN);
        for (User adminUser : adminUsers) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.INFO);
            notification.setUser(adminUser);
            notificationRepository.save(notification);
        }
    }







    @Override
    public void notifyAdminsAndAdminUsersAboutSupplierDeletion(String supplierName, String timestamp) {
        String message = "Supplier deleted: " + supplierName + " at " + timestamp;

        // Notify all admins
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.WARNING);
            notification.setAdmin(admin);
            notificationRepository.save(notification);
        }

        // Notify all users with the role "ADMIN"
        List<User> adminUsers = userRepository.findAllByRoleName(RoleType.ADMIN);
        for (User adminUser : adminUsers) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.WARNING);
            notification.setUser(adminUser);
            notificationRepository.save(notification);
        }
    }




    @Override
    public void notifyAdminsAndAdminUsersAboutSupplierUpdate(String message) { // update supplier in modal supplier details
        // Notify all admins
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.INFO);
            notification.setAdmin(admin);
            notificationRepository.save(notification);
        }

        // Notify all users with the role "ADMIN"
        List<User> adminUsers = userRepository.findAllByRoleName(RoleType.ADMIN);
        for (User adminUser : adminUsers) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.INFO);
            notification.setUser(adminUser);
            notificationRepository.save(notification);
        }
    }



    @Override
    public void notifyAdminsAndAdminUsersAboutCreateMaterial(String Name, String timestamp,String Admin) {
        String message = "Material added: " + Name +  " By Admin: " +Admin+ " at " + timestamp;
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.INFO);
            notification.setAdmin(admin);
            notificationRepository.save(notification);
        }

        // Notify all users with the role "ADMIN"
        List<User> adminUsers = userRepository.findAllByRoleName(RoleType.ADMIN);
        for (User adminUser : adminUsers) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.INFO);
            notification.setUser(adminUser);
            notificationRepository.save(notification);
        }
    }

    @Override
    public void notifyAdminsAndAdminUsersAboutDeleteMaterial(String Name, String timestamp,String Admin) {
        String message = "Material deleted: " + Name +  " By Admin: " +Admin+ " at " + timestamp;
        List<Admin> adminss = adminRepository.findAll();
        for (Admin admin : adminss) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.WARNING);
            notification.setAdmin(admin);
            notificationRepository.save(notification);
        }

        // Notify all users with the role "ADMIN"
        List<User> adminUsers = userRepository.findAllByRoleName(RoleType.ADMIN);
        for (User adminUser : adminUsers) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.WARNING);
            notification.setUser(adminUser);
            notificationRepository.save(notification);
        }
    }



    @Override
    public void notifyAdminsAndAdminUsersUpdateMaterial(String Name,String ref, String timestamp,String Admin) {
        String message = "Material updated: " + Name + " ,Ref: " + ref +  " By Admin: " +Admin+ " at " + timestamp;
        List<Admin> adminss = adminRepository.findAll();
        for (Admin admin : adminss) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.WARNING);
            notification.setAdmin(admin);
            notificationRepository.save(notification);
        }

        // Notify all users with the role "ADMIN"
        List<User> adminUsers = userRepository.findAllByRoleName(RoleType.ADMIN);
        for (User adminUser : adminUsers) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.WARNING);
            notification.setUser(adminUser);
            notificationRepository.save(notification);
        }
    }


}
