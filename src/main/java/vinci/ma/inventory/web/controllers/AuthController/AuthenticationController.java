package vinci.ma.inventory.web.controllers.AuthController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import vinci.ma.inventory.dao.entities.Admin;
import vinci.ma.inventory.dao.entities.Department;
import vinci.ma.inventory.dao.repositories.AdminRepo;
import vinci.ma.inventory.dao.repositories.DepartmentRepo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AuthenticationController {

    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private PasswordEncoder passwordEncoder; // Ensure this is properly autowired
    @Autowired
    private DepartmentRepo departmentRepo;

    @GetMapping("/login")
    public String showLogin() {
        return "signIn"; // returns the view name for the login page
    }

    @GetMapping("/register")
    public String showRegister(Model model) {

        List<Department> departments = departmentRepo.findAll();
        model.addAttribute("departments",departments);
        return "signUp"; // returns the view name for the registration page
    }

    @GetMapping("/register/validate")
    @ResponseBody
    public ResponseEntity<Map<String, String>> validateEditField(
            @RequestParam String field,
            @RequestParam String value) {
        Map<String, String> response = new HashMap<>();
        boolean isTaken = false;

        switch (field) {
            case "username" -> {
                isTaken = adminRepo.existsAdminByUsername(value);
                response.put("username", isTaken ? "Username is already taken" : "");
            }
            case "email" -> {
                isTaken = adminRepo.existsAdminByEmail(value);
                response.put("email", isTaken ? "Email is already in use" : "");
            }
            case "phone" -> {
                isTaken = adminRepo.existsAdminByPhone(value);
                response.put("phone", isTaken ? "Phone number is already in use" : "");
            }
            default -> {
                return ResponseEntity.badRequest().body(response);
            }
        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/register/add/admin")
    public String registerAdmin(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam("department") Long departmentId,
            @RequestParam("adminPicture") MultipartFile profilePic) {

        // Validate that the fields are not empty
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            return "redirect:/register?error=Please fill in all fields";
        }
        // Fetch department by ID
        Department department = departmentRepo.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // Check if the admin already exists (Optional: additional validation)
        if (adminRepo.findAdminByUsername(username)!= null) {
            return "redirect:/register?error=Username already exists";
        }

        // Handle profile picture
        byte[] profilePicData = null;
        if (!profilePic.isEmpty()) {
            try {
                profilePicData = profilePic.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/register?error=File upload failed";
            }
        }

        // Create a new Admin object
        Admin newAdmin = new Admin();
        newAdmin.setUsername(username);
        newAdmin.setEmail(email);
        newAdmin.setPhone(phone);
        newAdmin.setDepartment(department);
        newAdmin.setPassword(passwordEncoder.encode(password));
        newAdmin.setAdminPicture(profilePicData);

        // Save the new admin to the database
        adminRepo.save(newAdmin);


        return "redirect:/login?success=Registration successful";
    }

}
