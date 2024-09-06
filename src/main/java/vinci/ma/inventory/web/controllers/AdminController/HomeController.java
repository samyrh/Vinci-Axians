package vinci.ma.inventory.web.controllers.AdminController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vinci.ma.inventory.dao.entities.Admin;
import vinci.ma.inventory.dao.repositories.AdminRepo;

import java.util.Base64;

@Controller
public class HomeController {

    @Autowired
    private AdminRepo adminRepo;

    @GetMapping("/home/admin")
    public String showHomeAdmin(Model model) {
        // Get the authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the admin using the username
        Admin admin = adminRepo.findAdminByUsername(username);
        if (admin == null) {
            // Handle the case where the admin is not found
            model.addAttribute("error", "Admin not found");
            return "error"; // Ensure you have an error.html for this case
        }

        // Add admin details to the model
        model.addAttribute("admin", admin);
        if (admin.getAdminPicture() != null) {
            String base64Image = Base64.getEncoder().encodeToString(admin.getAdminPicture());
            model.addAttribute("adminPicture", "data:image/jpeg;base64," + base64Image);
        }

        return "home";
    }

}
