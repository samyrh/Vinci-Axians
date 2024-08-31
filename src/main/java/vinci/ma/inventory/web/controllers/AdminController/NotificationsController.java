package vinci.ma.inventory.web.controllers.AdminController;


import org.springframework.beans.factory.annotation.Autowired;
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
            Model model) {

        Optional<Admin> adminOptional = adminRepo.findById(1L);
        if (!adminOptional.isPresent()) {
            model.addAttribute("error", "Admin not found");
            return "error"; // Ensure you have an error.html for this case
        }

        Admin admin = adminOptional.get();

        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notificationPage = notificationRepo.findAllByAdminId(admin.getId(), pageable);

        model.addAttribute("notifications", notificationPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", notificationPage.getTotalPages());
        model.addAttribute("totalElements", notificationPage.getTotalElements());
        model.addAttribute("size", size); // Pass size to template for pagination controls

        return "notifications";
    }

    @GetMapping("/clear")
    public String clearNotifications() {
        // Find the admin by id
        Optional<Admin> admin = adminRepo.findById(1L);

        if (admin.isPresent()) {
            // Delete all notifications associated with this admin
            notificationRepo.deleteByAdminId(1L);
        }

        // Redirect to the notifications page after deletion
        return "redirect:/home/admin/notifications";
    }
}
