package vinci.ma.inventory.web.controllers.AdminController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vinci.ma.inventory.dao.entities.Admin;
import vinci.ma.inventory.dao.entities.Department;
import vinci.ma.inventory.dao.entities.Role;
import vinci.ma.inventory.dao.entities.User;
import vinci.ma.inventory.dao.enums.RoleType;
import vinci.ma.inventory.dao.repositories.AdminRepo;
import vinci.ma.inventory.dao.repositories.RoleRepo;
import vinci.ma.inventory.dao.repositories.UserRepo;
import vinci.ma.inventory.dto.DepartmentDTO;
import vinci.ma.inventory.services.managers.DepartmentManager;
import vinci.ma.inventory.services.managers.NotificationManager;
import vinci.ma.inventory.services.managers.RoleManager;
import vinci.ma.inventory.services.managers.UserManager;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/home/admin/users")
public class UsersController {

    @Autowired
    private RoleManager roleManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private NotificationManager notificationManager;
    @Autowired
    private AdminRepo adminRepository;









    @GetMapping
    public String showUserForm(Model model) {


        List<RoleType> roleList = Arrays.asList(RoleType.values());
        model.addAttribute("roles", roleRepo.findAll());
        model.addAttribute("departments", departmentManager.getAllDepartments());
        model.addAttribute("users",userManager.getAllUsers());



        List<Department> departments = departmentManager.getAllDepartments();
        List<DepartmentDTO> departmentDTOs = departments.stream().map(department -> {
            long totalUsers = userRepository.countByDepartmentId(department.getId());
            long totalAdmins = adminRepository.countByDepartmentId(department.getId());

            return new DepartmentDTO(department.getId(), department.getName(), totalUsers, totalAdmins);
        }).collect(Collectors.toList());

        model.addAttribute("departmentsDto", departmentDTOs);

        // Fetch the currently logged-in admin
        Authentication authenticationn = SecurityContextHolder.getContext().getAuthentication();
        String username = authenticationn.getName();
        Admin loggedInAdmin = adminRepository.findAdminByUsername(username);
        model.addAttribute("loggedInAdmin", loggedInAdmin);
        if (loggedInAdmin.getAdminPicture() != null) {
            String base1 = Base64.getEncoder().encodeToString(loggedInAdmin.getAdminPicture());
            model.addAttribute("adminPicture", "data:image/jpeg;base64," + base1);
        }
        return "users";
    }









    @PostMapping("/add")
    public String addUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam Long departmentId,
            @RequestParam Long roleId,
            @RequestParam(required = false) MultipartFile profilePicture) throws IOException {

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setAddress(address);

        // Handle profile picture upload
        if (profilePicture != null && !profilePicture.isEmpty()) {
            byte[] profilePictureBytes = profilePicture.getBytes();
            user.setProfilePicture(profilePictureBytes);
        } else {
            user.setProfilePicture(null); // Optional, could also default to a placeholder
        }

        // Fetch department and role entities
        Department department = departmentManager.getDepartmentById(departmentId);
        Role role = roleManager.getRoleById(roleId);

        user.setDepartment(department);
        user.setRole(role);

        // Save the user
        userManager.createUser(user);

        return "redirect:/home/admin/users"; // Redirect to user list after creation
    }


    @GetMapping("/check")
    @ResponseBody
    public String checkExistingUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String lastName
           ) {

        List<User> existingUsers = userRepo.findAll();
        boolean usernameExists = existingUsers.stream().anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
        boolean emailExists = existingUsers.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
        boolean phoneExists = existingUsers.stream().anyMatch(user -> user.getPhone().equalsIgnoreCase(phone));
        boolean lastNameExists = existingUsers.stream().anyMatch(user -> user.getLastName().equalsIgnoreCase(lastName));

        return String.format("{\"usernameExists\": %b, \"emailExists\": %b, \"phoneExists\": %b, \"lastNameExists\": %b}",
                usernameExists, emailExists, phoneExists, lastNameExists);
    }





    @GetMapping("/delete/{userId}")
    public String deleteUser(@PathVariable Long userId, Model model) {
        try {
            userManager.deleteUser(userId);
            model.addAttribute("message", "User deleted successfully.");
            return "redirect:/home/admin/users"; // Redirect to user list page
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // Error view
        }
    }




    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        // Fetch the user by ID
        User user = userRepo.findUserById(id);

        if (user == null) {
            // Handle user not found, maybe redirect or return an error page
            return "redirect:/error"; // Example redirection; adjust as needed
        }

        // Convert the profile picture to a Base64 string if it exists
        String base64Image = user.getProfilePicture() != null
                ? "data:image/png;base64," + Base64.getEncoder().encodeToString(user.getProfilePicture())
                : "https://bootdey.com/img/Content/avatar/avatar7.png";

        model.addAttribute("profilePicture", base64Image);
        model.addAttribute("user", user);

        // Fetch other data as needed
        List<Department> departments = departmentManager.getAllDepartments();
        List<Role> roles = roleRepo.findAll();

        model.addAttribute("departments", departments);
        model.addAttribute("roles", roles);
        // Fetch the currently logged-in admin
        Authentication authenticationn = SecurityContextHolder.getContext().getAuthentication();
        String username = authenticationn.getName();
        Admin loggedInAdmin = adminRepository.findAdminByUsername(username);
        model.addAttribute("loggedInAdmin", loggedInAdmin);
        if (loggedInAdmin.getAdminPicture() != null) {
            String base1 = Base64.getEncoder().encodeToString(loggedInAdmin.getAdminPicture());
            model.addAttribute("adminPicture", "data:image/jpeg;base64," + base1);
        }
        return "edituser"; // Thymeleaf template name
    }




    @PostMapping("/update")
    public String updateUser(@RequestParam("userId") Long userId,
                             @RequestParam("username") String username,
                             @RequestParam("email") String email,
                             @RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             @RequestParam("phone") String phone,
                             @RequestParam("address") String address,
                             @RequestParam("departmentId") Long departmentId,
                             @RequestParam("roleId") Long roleId,
                             @RequestParam("profilePicture") MultipartFile profilePicture,
                             RedirectAttributes redirectAttributes) {

        // Find existing user
        User existingUser = userManager.getUserById(userId);


        // Update user details
        existingUser.setUsername(username);
        existingUser.setEmail(email);
        existingUser.setFirstName(firstName);
        existingUser.setLastName(lastName);
        existingUser.setPhone(phone);
        existingUser.setAddress(address);

        // Update department and role
        Department department = departmentManager.getDepartmentById(departmentId);
        Role role = roleManager.getRoleById(roleId);

        if (department != null && role != null) {
            existingUser.setDepartment(department);
            existingUser.setRole(role);
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid department or role.");
            return "redirect:/home/admin/users/edit/" + userId;
        }

        // Handle profile picture update
        if (!profilePicture.isEmpty()) {
            try {
                existingUser.setProfilePicture(profilePicture.getBytes());
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Failed to upload profile picture.");
                return "redirect:/home/admin/users/edit/" + userId;
            }
        }

        // Save the updated user
        userRepo.save(existingUser);
        User us1 = userRepo.findUserById(userId);
        notificationManager.notifyProfileUpdate(us1);

        redirectAttributes.addFlashAttribute("message", "User updated successfully.");
        return "redirect:/home/admin/users";
    }


    @GetMapping("/edit/validate")
    public ResponseEntity<Map<String, String>> validateEditField(@RequestParam String field, @RequestParam String value, @RequestParam Long userId) {
        Map<String, String> response = new HashMap<>();
        boolean isTaken = false;

        switch (field) {
            case "username":
                isTaken = userManager.isUsernameTaken(value, userId);
                response.put("username", isTaken ? "Username is already taken" : "");
                break;
            case "email":
                isTaken = userManager.isEmailTaken(value, userId);
                response.put("email", isTaken ? "Email is already in use" : "");
                break;
            case "phone":
                isTaken = userManager.isPhoneTaken(value, userId);
                response.put("phone", isTaken ? "Phone number is already in use" : "");
                break;
            default:
                return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/details/{userId}")
    public String getUserDetails(@PathVariable("userId") Long userId, Model model) {
        User user = userManager.getUserById(userId);
        if (user != null) {
            // Convert the profile picture to a Base64 string
            String base64Image = user.getProfilePicture() != null
                    ? "data:image/png;base64," + Base64.getEncoder().encodeToString(user.getProfilePicture())
                    : "https://bootdey.com/img/Content/avatar/avatar7.png";

            model.addAttribute("userDetail", user);
            model.addAttribute("profilePicture", base64Image);
            // Fetch the currently logged-in admin
            Authentication authenticationn = SecurityContextHolder.getContext().getAuthentication();
            String username = authenticationn.getName();
            Admin loggedInAdmin = adminRepository.findAdminByUsername(username);
            model.addAttribute("loggedInAdmin", loggedInAdmin);
            if (loggedInAdmin.getAdminPicture() != null) {
                String base1 = Base64.getEncoder().encodeToString(loggedInAdmin.getAdminPicture());
                model.addAttribute("adminPicture", "data:image/jpeg;base64," + base1);
            }
            return "userDetails"; // Thymeleaf template name without .html extension
        } else {
            return "error"; // Handle user not found
        }
    }
}
