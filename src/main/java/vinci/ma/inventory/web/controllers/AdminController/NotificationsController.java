package vinci.ma.inventory.web.controllers.AdminController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vinci.ma.inventory.dao.entities.Admin;
import vinci.ma.inventory.dao.entities.Notification;
import vinci.ma.inventory.dao.repositories.AdminRepo;
import vinci.ma.inventory.dao.repositories.NotificationRepo;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home/admin/notifications")
public class NotificationsController {


    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private NotificationRepo notificationRepo;

    @GetMapping
    public String showNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Authentication authentication, // Add this parameter
            Model model) {

        // Retrieve the currently logged-in admin's username
        String username = authentication.getName();

        // Fetch the admin using the username
        Admin admin = adminRepo.findAdminByUsername(username);
        if (admin == null) {
            model.addAttribute("error", "Admin not found");
            return "error"; // Ensure you have an error.html for this case
        }
        model.addAttribute("loggedInAdmin", admin);
        if (admin.getAdminPicture() != null) {
            String base1 = Base64.getEncoder().encodeToString(admin.getAdminPicture());
            model.addAttribute("adminPicture", "data:image/jpeg;base64," + base1);
        }
        // Set up pagination
        Pageable pageable = PageRequest.of(page, size);

        // Fetch notifications for the logged-in admin
        Page<Notification> notificationPage = notificationRepo.findAllByAdminId(admin.getId(), pageable);

        model.addAttribute("notifications", notificationPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", notificationPage.getTotalPages());
        model.addAttribute("totalElements", notificationPage.getTotalElements());
        model.addAttribute("size", size); // Pass size to template for pagination controls


        return "notifications";
    }


    @GetMapping("/clear")
    public String clearNotifications(Authentication authentication) {
        // Retrieve the currently logged-in admin's username
        String username = authentication.getName();

        // Fetch the admin using the username
        Admin admin = adminRepo.findAdminByUsername(username);
        if (admin == null) {
            // Handle the case where the admin is not found, if necessary
            return "error"; // Ensure you have an error.html for this case
        }

        // Delete all notifications associated with this admin
        notificationRepo.deleteByAdminId(admin.getId());

        // Redirect to the notifications page after deletion
        return "redirect:/home/admin/notifications";
    }

}
