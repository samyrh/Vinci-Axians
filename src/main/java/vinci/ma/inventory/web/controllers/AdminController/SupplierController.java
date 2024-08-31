package vinci.ma.inventory.web.controllers.AdminController;


import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vinci.ma.inventory.dao.entities.Material;
import vinci.ma.inventory.dao.entities.Supplier;
import vinci.ma.inventory.dao.repositories.MaterialRepo;
import vinci.ma.inventory.dao.repositories.SupplierRepo;
import vinci.ma.inventory.services.managers.MaterialManager;
import vinci.ma.inventory.services.managers.NotificationManager;
import vinci.ma.inventory.services.managers.SupplierManager;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/home/admin/suppliers")
public class SupplierController {

    @Autowired
    private SupplierManager supplierManager;
    @Autowired
    private SupplierRepo supplierRepo;
    @Autowired
    private NotificationManager notificationManager;

    @Autowired
    private MaterialRepo materialRepo;
    @Autowired
    private MaterialManager materialManager;


    @GetMapping
    public String showSuppliers(Model model) {
        List<Supplier> suppliers = supplierManager.getAllSuppliers();
        List<String> badgeColors = Arrays.asList(
                "badge badge-primary",
                "badge badge-warning",
                "badge badge-danger",
                "badge badge-success",
                "badge badge-info"
        );

        model.addAttribute("suppliers", suppliers);
        model.addAttribute("badgeColors", badgeColors);

        return "suppliers"; // Ensure this is the name of your Thymeleaf template file
    }


    @GetMapping("/validate")
    public ResponseEntity<Map<String, String>> validateSupplierField(@RequestParam String fieldName, @RequestParam String value) {
        Map<String, String> response = new HashMap<>();
        boolean isTaken;

        switch (fieldName) {
            case "fullName":
                isTaken = supplierManager.isFullNameTaken(value);
                response.put("fullName", isTaken ? "Full name is already taken" : "");
                break;
            case "phone":
                isTaken = supplierManager.isPhoneTaken(value);
                response.put("phone", isTaken ? "Phone number is already taken" : "");
                break;
            case "supplierCode":
                isTaken = supplierManager.isSupplierCodeTaken(value);
                response.put("supplierCode", isTaken ? "Supplier code is already taken" : "");
                break;
            case "contactEmail":
                isTaken = supplierManager.isContactEmailTaken(value);
                response.put("contactEmail", isTaken ? "Contact email is already taken" : "");
                break;
            default:
                response.put(fieldName, "Invalid field");
                break;
        }

        return ResponseEntity.ok(response);
    }




    @PostMapping("/add")
    public String addSupplier(@ModelAttribute Supplier supplier, RedirectAttributes redirectAttributes) {
        // Save the new supplier
        supplierRepo.save(supplier);

        // Notify all admins and admin users
        String timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
        notificationManager.notifyAdminsAndAdminUsersAboutSupplierAddition(supplier.getFullName(), timestamp);

        redirectAttributes.addFlashAttribute("message", "Supplier added successfully!");

        return "redirect:/home/admin/suppliers"; // Redirect to the supplier list page
    }




    @GetMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Delete the supplier
            supplierManager.deleteSupplier(id);

            // Optionally, notify admins and users with role ADMIN
            Supplier supplier = supplierManager.getSupplierById(id); // Fetch the supplier to get its name
            String supplierName = supplier.getFullName();
            String timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
            notificationManager.notifyAdminsAndAdminUsersAboutSupplierDeletion(supplierName, timestamp);

            // Add a success message
            redirectAttributes.addFlashAttribute("message", "Supplier deleted successfully.");

        } catch (EntityNotFoundException e) {
            // Add an error message
            redirectAttributes.addFlashAttribute("errorMessage", "Supplier not found.");
        } catch (Exception e) {
            // Handle unexpected errors
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred.");
        }

        // Redirect to the supplier list or another appropriate page
        return "redirect:/home/admin/suppliers";
    }



    @GetMapping("/supplier/details/{id}")
    public String getSupplierDetails(@PathVariable("id") Long id, Model model) {
        Supplier supplier = supplierManager.getSupplierById(id);
        if (supplier == null) {
            return "redirect:/home/admin/suppliers"; // or show an error page
        }
        model.addAttribute("supplier", supplier);

        List<Material> materials = materialRepo.findBySupplierId(id);
        // Convert images to Base64
        for (Material material : materials) {
            if (material.getImage() != null) {
                try {
                    String base64Image = Base64.getEncoder().encodeToString(material.getImage());
                    material.setImageBase64(Arrays.toString(material.getImage()));
                } catch (Exception e) {
                    e.printStackTrace(); // Log any conversion errors
                }
            }
        }
        model.addAttribute("materials", materials);
        return "supplierDetails";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable Long id) {
        Material material = materialRepo.findById(id).orElse(null);
        if (material == null || material.getImage() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ByteArrayResource resource = new ByteArrayResource(material.getImage());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Adjust based on your image type
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg")
                .body(resource);
    }

    @GetMapping("/supplier/details/validate")
    public ResponseEntity<Map<String, String>> validateSupplierEditPage(
            @RequestParam String fieldName,
            @RequestParam String value) {

        Map<String, String> response = new HashMap<>();
        boolean isTaken = false;

        switch (fieldName) {
            case "fullName":
                isTaken = supplierManager.isFullNameTaken(value);
                response.put(fieldName, isTaken ? "Full name is already in use" : "");
                break;
            case "phone":
                isTaken = supplierManager.isPhoneTaken(value);
                response.put(fieldName, isTaken ? "Phone number is already in use" : "");
                break;
            case "supplierCode":
                isTaken = supplierManager.isSupplierCodeTaken(value);
                response.put(fieldName, isTaken ? "Supplier code is already in use" : "");
                break;
            case "contactEmail":
                isTaken = supplierManager.isContactEmailTaken(value);
                response.put(fieldName, isTaken ? "Email is already in use" : "");
                break;
            default:
                response.put(fieldName, "");
        }

        return ResponseEntity.ok(response);
    }





    @PostMapping("/supplier/details/update")
    public String updateSupplier(
            @RequestParam Long supplierId,
            @RequestParam String fullName,
            @RequestParam String phone,
            @RequestParam String supplierCode,
            @RequestParam String contactEmail,
            @RequestParam String address,
            @RequestParam String description,
            RedirectAttributes redirectAttributes) {

        try {
            // Fetch the current supplier details before update
            Supplier oldSupplier = supplierManager.getSupplierById(supplierId);

            // Update supplier details
            supplierManager.updateSupplier(supplierId, fullName, phone, supplierCode, contactEmail, address, description);

            // Prepare notification message
            String timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
            String message = "Supplier updated: " + fullName + " ,Code: " + supplierCode + "    at " + timestamp;

            // Notify admins and users with the role "ADMIN"
            notificationManager.notifyAdminsAndAdminUsersAboutSupplierUpdate(message);

            // Set success message for redirect
            redirectAttributes.addFlashAttribute("message", "Supplier updated successfully.");

            // Redirect to usersDetails page
            return "redirect:/home/admin/suppliers/supplier/details/" + supplierId;
        } catch (Exception e) {
            // Set error message for redirect
            redirectAttributes.addFlashAttribute("error", "Error updating supplier: " + e.getMessage());

            // Redirect back to the form page
            return "redirect:/home/admin/suppliers/supplier/details/" + supplierId;
        }

    }
}

