package vinci.ma.inventory.services.managers;

import vinci.ma.inventory.dao.entities.Notification;
import vinci.ma.inventory.dao.entities.User;

import java.util.List;

public interface NotificationManager {


    Notification createNotification(Notification notification);
    Notification getNotificationById(Long id);
    List<Notification> getAllNotifications();
    void deleteNotification(Long id);
    void sendNotification(Long notificationId);
    void notifyUser(User user);
    void notifyAdmins(User user);
    void notifyAdminsAndAdminUsersAboutDeletion(User deletedUser);

    void notifyProfileUpdate(User updatedUser);

    void notifyAdminsAndAdminUsersAboutDepartmentDeletion(String departmentName);

    void notifyAdminsAndAdminUsersAboutDepartmentCreation(String departmentName, String timestamp);

    void notifyAdminsAndAdminUsersAboutCategoryCreation(String categoryName, String timestamp);

    void notifyAdminsAndAdminUsersAboutCategoryDeletion(String categoryName, String timestamp);


    void notifyAdminsAndAdminUsersAboutCategoryUpdate(String categoryName, String timestamp);


    void notifyAdminsAndAdminUsersAboutSupplierAddition(String supplierName, String timestamp);

    void notifyAdminsAndAdminUsersAboutSupplierDeletion(String supplierName, String timestamp);
    void notifyAdminsAndAdminUsersAboutSupplierUpdate(String message);

    void notifyAdminsAndAdminUsersAboutCreateMaterial(String Name, String timestamp, String Admin);

    void notifyAdminsAndAdminUsersAboutDeleteMaterial(String Name, String timestamp, String Admin);

    void notifyAdminsAndAdminUsersUpdateMaterial(String Name, String ref, String timestamp, String Admin);
}
