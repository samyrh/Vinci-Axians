package vinci.ma.inventory.web.controllers.AdminController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vinci.ma.inventory.dao.entities.Admin;
import vinci.ma.inventory.dao.entities.Category;
import vinci.ma.inventory.dao.entities.Supplier;
import vinci.ma.inventory.dao.repositories.AdminRepo;
import vinci.ma.inventory.dao.repositories.CategoryRepo;
import vinci.ma.inventory.dao.repositories.DepartmentRepo;
import vinci.ma.inventory.dao.repositories.SupplierRepo;

import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private SupplierRepo supplierRepo;
    @Autowired
    private DepartmentRepo departmentRepo;


    @GetMapping("/home/admin")
    public String showHomeAdmin(Model model) {
        // Get the authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the admin using the username
        Admin admin = adminRepo.findAdminByUsername(username);
        if (admin == null) {
            model.addAttribute("error", "Admin not found");
            return "error";
        }

        // Add admin details to the model
        model.addAttribute("admin", admin);
        if (admin.getAdminPicture() != null) {
            String base64Image = Base64.getEncoder().encodeToString(admin.getAdminPicture());
            model.addAttribute("adminPicture", "data:image/jpeg;base64," + base64Image);
        }



        return "home";
    }
    @GetMapping("/category-data")
    @ResponseBody
    public List<Map<String, Object>> getCategoryData() {
        return categoryRepo.getCategoryData();
    }

    // Endpoint to fetch supplier data
    @GetMapping("/supplier-data")
    @ResponseBody
    public List<Map<String, Object>> getSupplierData() {
        return supplierRepo.getSupplierData();
    }

    // Endpoint to fetch department data
    @GetMapping("/department-data")
    @ResponseBody
    public List<Map<String, Object>> getDepartmentData() {
        return departmentRepo.getDepartmentData();
    }

}
