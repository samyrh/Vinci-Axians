package vinci.ma.inventory.web.controllers.AdminController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vinci.ma.inventory.dao.entities.Admin;
import vinci.ma.inventory.dao.entities.Notification;
import vinci.ma.inventory.dao.enums.NotificationType;
import vinci.ma.inventory.dao.repositories.AdminRepo;
import vinci.ma.inventory.dao.repositories.NotificationRepo;
import vinci.ma.inventory.services.managers.AdminManager;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ProfileController {

    @Autowired
    private AdminManager adminManager;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private NotificationRepo notificationRepo;

    @GetMapping("/home/admin/profil")
    public String showProfileAdmin(Model model) {

        Admin admin = adminManager.getAdminById(1L);
        model.addAttribute("admin",admin);
        if (admin.getAdminPicture() != null) {
            String base64Image = Base64.getEncoder().encodeToString(admin.getAdminPicture());
            model.addAttribute("adminPicture", "data:image/jpeg;base64," + base64Image);
        }
        return "profil";
    }


    @GetMapping("/home/admin/profil/edit")
    public String showProfilEditAdmin(Model model) {

        Admin admin = adminManager.getAdminById(1L);
        model.addAttribute("admin",admin);
        return "profiledit";
    }
    @PostMapping("/home/admin/profil/update")
    public String updateProfile(
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("profilePicture") MultipartFile profilePicture,
            RedirectAttributes redirectAttributes) {

        try {
            // Update admin details
            Admin admin = adminManager.getAdminById(1L);
            admin.setEmail(email);
            admin.setPhone(phone);
            admin.setUsername(username);
            admin.setPassword(password);


            if (!profilePicture.isEmpty()) {
                // Save the uploaded file
                byte[] pictureBytes = profilePicture.getBytes();
                admin.setAdminPicture(pictureBytes);
            }

            adminRepo.save(admin);

            String timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());

            String message = "Admin : " + admin.getUsername() + " ,Department : " + admin.getDepartment().getName() +  " ,updated his profile at : " + timestamp;

            List<Admin> adminsToNotify = adminManager.getAllAdmins();
            for (Admin adminn : adminsToNotify) {
                if (!adminn.equals(admin)) {
                    Notification notification = new Notification();
                    notification.setMessage(message);
                    notification.setDate(new Date());
                    notification.setType(NotificationType.INFO);
                    notification.setAdmin(adminn);
                    notificationRepo.save(notification);
                }
            }

            redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile: " + e.getMessage());
        }

        return "redirect:/home/admin/profil/edit";
    }

    @GetMapping("/home/admin/profil/validateUpdate")
    public ResponseEntity<Map<String, String>> validateAdminUpdateFields(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String password
    ) {
        Map<String, String> response = new HashMap<>();

        // Fetch all admins from the repository
        List<Admin> allAdmins = adminRepo.findAll();

        // Debugging: Check the size of the list and log it
        System.out.println("Number of admins fetched: " + allAdmins.size());

        // Validate username
        if (username != null && !username.trim().isEmpty()) {
            boolean usernameTaken = allAdmins.stream()
                    .anyMatch(admin -> admin.getUsername().equalsIgnoreCase(username));
            if (usernameTaken) {
                response.put("username", "Username is already taken.");
            }
        }

        // Validate email
        if (email != null && !email.trim().isEmpty()) {
            boolean emailTaken = allAdmins.stream()
                    .anyMatch(admin -> admin.getEmail().equalsIgnoreCase(email));
            if (emailTaken) {
                response.put("email", "Email is already taken.");
            }
        }

        // Validate phone
        if (phone != null && !phone.trim().isEmpty()) {
            boolean phoneTaken = allAdmins.stream()
                    .anyMatch(admin -> admin.getPhone().equalsIgnoreCase(phone));
            if (phoneTaken) {
                response.put("phone", "Phone number is already taken.");
            }
        }

        // Validate password
        if (password != null && password.trim().isEmpty()) {
            response.put("password", "Password cannot be empty.");
        }

        return ResponseEntity.ok(response);
    }
}
