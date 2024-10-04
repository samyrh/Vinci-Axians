package vinci.ma.inventory.web.controllers.AdminController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vinci.ma.inventory.dao.entities.Admin;
import vinci.ma.inventory.dao.entities.Category;
import vinci.ma.inventory.dao.repositories.AdminRepo;
import vinci.ma.inventory.dao.repositories.CategoryRepo;
import vinci.ma.inventory.services.managers.CategoryManager;
import vinci.ma.inventory.services.managers.NotificationManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/home/admin/category")
public class CategoriesController {

    @Autowired
    private CategoryManager categoryManager;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private NotificationManager notificationManager;
    @Autowired
    private AdminRepo adminRepo;



    @GetMapping
    public String getCategories(Model model) {
        List<Category> categories = categoryManager.getAllCategories();
        List<String> badgeColors = Arrays.asList("badge badge-primary", "badge badge-warning", "badge badge-danger", "badge badge-success", "badge badge-info");


        model.addAttribute("categories", categories);
        model.addAttribute("badgeColors", badgeColors);
        // Fetch the currently logged-in admin
        Authentication authenticationn = SecurityContextHolder.getContext().getAuthentication();
        String username = authenticationn.getName();
        Admin loggedInAdmin = adminRepo.findAdminByUsername(username);
        model.addAttribute("loggedInAdmin", loggedInAdmin);
        if (loggedInAdmin.getAdminPicture() != null) {
            String base64Image = Base64.getEncoder().encodeToString(loggedInAdmin.getAdminPicture());
            model.addAttribute("adminPicture", "data:image/jpeg;base64," + base64Image);
        }
        return "categories"; // Ensure this is the name of your Thymeleaf template file
    }






    @GetMapping("/validate")  // if category name is token or not in modal page categories
    public ResponseEntity<Map<String, String>> validateCategoryName(@RequestParam String name) {
        Map<String, String> response = new HashMap<>();
        boolean isTaken = categoryManager.isCategoryNameTaken(name);
        response.put("isTaken", isTaken ? "Category name is already taken" : "");
        return ResponseEntity.ok(response);
    }




    @PostMapping("/add")
    public String addCategory(@RequestParam("name") String name,
                              @RequestParam("description") String description,
                              @RequestParam("categoryPicture") MultipartFile picture,
                              RedirectAttributes redirectAttributes) {
        // Validate input
        if (name == null || name.trim().isEmpty() ||
                description == null || description.trim().isEmpty() ||
                picture.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "All fields are required.");
            return "redirect:/home/admin/category"; // Redirect to the category page or where appropriate
        }

        // Create new category object
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);

        // Handle picture upload
        try {
            if (!picture.isEmpty()) {
                category.setCategoryPicture(picture.getBytes());
            }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload picture.");
            return "redirect:/home/admin/category"; // Redirect to the category page or where appropriate
        }

        // Save category to the database
        categoryRepo.save(category);

        // Notify admins and admin users about the new category creation
        String timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
        notificationManager.notifyAdminsAndAdminUsersAboutCategoryCreation(name, timestamp);

        // Redirect with success message
        redirectAttributes.addFlashAttribute("success", "Category added successfully.");
        return "redirect:/home/admin/category"; // Redirect to the category page or where appropriate
    }






    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Find the category
            Category category = categoryManager.getCategoryById(id);

            if (category == null) {
                redirectAttributes.addFlashAttribute("error", "Category not found");
                return "redirect:/home/admin/category";
            }

            // Extract category name for notification
            String categoryName = category.getName();
            String timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());

            // Delete the category
            categoryManager.deleteCategory(id);

            // Notify admins and users with role ADMIN
            notificationManager.notifyAdminsAndAdminUsersAboutCategoryDeletion(categoryName, timestamp);

            redirectAttributes.addFlashAttribute("message", "Category deleted successfully");
        } catch (DataIntegrityViolationException e) {
            // Handle specific database constraint violations
            redirectAttributes.addFlashAttribute("error", "Failed to delete category due to existing dependencies");
        } catch (Exception e) {
            // Handle general exceptions
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred");
        }

        return "redirect:/home/admin/category"; // Redirect to category list after deletion
    }







    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) throws IOException {

        Category category = categoryManager.getCategoryById(id);

        if (category != null) {
            // Convert the category picture to a Base64 string if it exists
            String base64Image = category.getCategoryPicture() != null
                    ? "data:image/png;base64," + Base64.getEncoder().encodeToString(category.getCategoryPicture())
                    : "https://bootdey.com/img/Content/avatar/avatar7.png";

            model.addAttribute("category", category);
            model.addAttribute("categoryPicture", base64Image); // Add Base64 image to the model

            List<Category> categories = categoryRepo.findAll();
            model.addAttribute("categories", categories);

            // Fetch the currently logged-in admin
            Authentication authenticationn = SecurityContextHolder.getContext().getAuthentication();
            String username = authenticationn.getName();
            Admin loggedInAdmin = adminRepo.findAdminByUsername(username);
            model.addAttribute("loggedInAdmin", loggedInAdmin);
            if (loggedInAdmin.getAdminPicture() != null) {
                String base1 = Base64.getEncoder().encodeToString(loggedInAdmin.getAdminPicture());
                model.addAttribute("adminPicture", "data:image/jpeg;base64," + base1);
            }
            return "categoryEdit"; // The name of your HTML template
        }




        return "redirect:/home/admin/category"; // Redirect if category not found
    }


    @GetMapping("/edit/validation")
    public ResponseEntity<Map<String, String>> validateCategoryNameEditPage(@RequestParam String name) {
        Map<String, String> response = new HashMap<>();
        boolean isTaken = categoryManager.isCategoryNameTaken(name);

        response.put("name", isTaken ? "Category name is already in use" : "");

        return ResponseEntity.ok(response);
    }



    @PostMapping("/update")
    public String updateCategory(@RequestParam("id") Long categoryId,
                                 @RequestParam("name") String name,
                                 @RequestParam("description") String description,
                                 @RequestParam(value = "categoryPicture", required = false) MultipartFile categoryPicture,
                                 RedirectAttributes redirectAttributes) {

        // Find existing category
        Category existingCategory = categoryManager.getCategoryById(categoryId);

        if (existingCategory == null) {
            redirectAttributes.addFlashAttribute("error", "Category not found.");
            return "redirect:/home/admin/category/edit/" + categoryId;
        }

        // Update category details
        existingCategory.setName(name);
        existingCategory.setDescription(description);

        // Handle category picture update
        if (categoryPicture != null && !categoryPicture.isEmpty()) {
            try {
                existingCategory.setCategoryPicture(categoryPicture.getBytes());
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Failed to upload category picture.");
                return "redirect:/home/admin/category/edit/" + categoryId;
            }
        }

        // Save the updated category
        categoryRepo.save(existingCategory);

        // Notify admins about the update
        String timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
        notificationManager.notifyAdminsAndAdminUsersAboutCategoryUpdate(name, timestamp);

        redirectAttributes.addFlashAttribute("message", "Category updated successfully.");
        return "redirect:/home/admin/category/edit/" + categoryId;
    }
}
