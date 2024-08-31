package vinci.ma.inventory.web.controllers.AdminController;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vinci.ma.inventory.dao.entities.*;
import vinci.ma.inventory.dao.enums.NotificationType;
import vinci.ma.inventory.dao.repositories.*;
import vinci.ma.inventory.dto.MaterialSupplierDTO;
import vinci.ma.inventory.services.managers.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home/admin/material")
public class MaterialController {

    @Autowired
    private CategoryManager categoryManger;
    @Autowired
    private SupplierManager supplierManager;
    @Autowired
    private MaterialRepo materialRepo;
    @Autowired
    private MaterialManager materialManager;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private SupplierRepo supplierRepo;
    @Autowired
    private AdminManager adminManager;
    @Autowired
    private NotificationManager notificationManager;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private CommentRepo commentRepo;




    @GetMapping
    public String showListOfMaterials(
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            Model model
    ) {
        List<Category> categories = categoryManger.getAllCategories();
        model.addAttribute("categories", categories);

        List<Material> materials;
        if (categoryId != null) {
            // Fetch materials by selected category
            materials = materialRepo.findMaterialsByCategoryId(categoryId);
        } else {
            // Fetch all materials if no category is selected
            materials = materialRepo.findAll();
        }

        for (Material material : materials) {
            if (material.getImage() != null) {
                try {
                    String base64Image = Base64.getEncoder().encodeToString(material.getImage());
                    material.setImageBase64("data:image/jpeg;base64," + base64Image); // Proper base64 encoding
                } catch (Exception e) {
                    e.printStackTrace(); // Log any conversion errors
                }
            }
        }
        model.addAttribute("materials", materials);

        List<Supplier> suppliers = supplierManager.getAllSuppliers();
        model.addAttribute("suppliers", suppliers);

        List<String> badgeColors = Arrays.asList("badge badge-primary", "badge badge-warning", "badge badge-danger", "badge badge-success", "badge badge-info");
        List<MaterialSupplierDTO> materialSupplierData = materialManager.getAllMaterialSupplierData();
        model.addAttribute("materialSupplierData", materialSupplierData);

        // Group materials by category
        Map<Category, List<Material>> materialsByCategory = materials.stream()
                .collect(Collectors.groupingBy(Material::getCategory));
        model.addAttribute("materialsByCategory", materialsByCategory);

        return "material";
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

    @GetMapping("/home/admin/material/edit")
    public String showEditMaterialPage() {


        return "materialEdit";
    }



    @PostMapping("/add")
    public String addMaterial(@RequestParam("name") String name,
                              @RequestParam("description") String description,
                              @RequestParam("price") Double price,
                              @RequestParam("stock") Integer stock,
                              @RequestParam("ref") String ref,
                              @RequestParam("categoryId") Long categoryId,
                              @RequestParam("supplierId") List<Long> supplierIds,
                              @RequestParam("image") MultipartFile image) throws IOException {

        Material material = new Material();
        material.setName(name);
        material.setDescription(description);
        material.setPrice(price);
        material.setStock(stock);
        material.setRef(ref);

        // Set the category
        Category category = categoryManger.getCategoryById(categoryId);
        if (category != null) {
            material.setCategory(category);
        } else {

            return "redirect:/home/admin/material";
        }

        // Set the suppliers
        List<Supplier> suppliers = supplierRepo.findAllById(supplierIds);
        material.setSuppliers(suppliers);

        // Handle image upload
        if (!image.isEmpty()) {
            try {
                material.setImage(image.getBytes());
            } catch (IOException e) {

                return "redirect:/home/admin/material";
            }
        }

        material.setDateEntered(new Date());

        // Save the material
        Material savedMaterial = materialRepo.save(material);

        // Link the material with the admin
        Admin admin = adminManager.getAdminById(1L);
        admin.getMaterials().add(savedMaterial);
        adminRepo.save(admin);

        // Notify admins and admin users about the new category creation
        String timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
        notificationManager.notifyAdminsAndAdminUsersAboutCreateMaterial(name,timestamp,admin.getUsername());

        return "redirect:/home/admin/material";
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, String>> validateMaterialFields(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String stock,
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) String image // Add image parameter for validation
    ) {
        Map<String, String> response = new HashMap<>();

        // Validate name
        if (name == null || name.trim().isEmpty()) {
            response.put("name", "Name is required");
        }

        // Validate description
        if (description == null || description.trim().isEmpty()) {
            response.put("description", "Description is required");
        }

        // Validate price
        if (price == null || price.trim().isEmpty()) {
            response.put("price", "Price is required");
        }

        // Validate stock
        if (stock == null || stock.trim().isEmpty()) {
            response.put("stock", "Stock is required");
        }

        // Validate reference
        if (reference == null || reference.trim().isEmpty()) {
            response.put("reference", "Reference is optional but should not be empty if provided");
        }

        // Validate image
        if (image == null || image.trim().isEmpty()) {
            response.put("image", "Image is required");
        }

        return ResponseEntity.ok(response);
    }



    @GetMapping("/delete/{id}")
    public String deleteMaterial(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Admin admin = adminManager.getAdminById(1L);
            Material deletedMaterial = materialManager.getMaterialById(id);
            // Notify admins and admin users about the new category creation
            String timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
            notificationManager.notifyAdminsAndAdminUsersAboutDeleteMaterial(deletedMaterial.getName(),timestamp,admin.getUsername());


            materialManager.deleteMaterial(id);
            redirectAttributes.addFlashAttribute("message", "Material deleted successfully.");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Material not found.");
        }

        return "redirect:/home/admin/material";
    }



    @GetMapping("/details/{id}")
    public String showMaterialDetails(@PathVariable Long id, Model model) {
        MaterialSupplierDTO materialDetails = materialManager.getMaterialSupplierDetails(id);

        if (materialDetails == null) {
            return "redirect:/error";
        }

        List<Comment> comments = commentRepo.findAllByMaterialId(id);
        List<Category> categories = categoryManger.getAllCategories();
        List<Supplier> suppliers = supplierRepo.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("material", materialDetails);
        model.addAttribute("comments", comments);

        return "materialDetails";
    }



    @GetMapping("/validateUpdate")
    public ResponseEntity<Map<String, String>> validateMaterialFieldsDetails(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String stock,
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) String image // Image can be represented by a URL or a placeholder
    ) {
        Map<String, String> response = new HashMap<>();

        // Validate name
        if (name == null || name.trim().isEmpty()) {
            response.put("name", "Name is required");
        }

        // Validate description
        if (description == null || description.trim().isEmpty()) {
            response.put("description", "Description is required");
        }

        // Validate price
        if (price == null || price.trim().isEmpty()) {
            response.put("price", "Price is required");
        } else {
            try {
                Double.parseDouble(price);
            } catch (NumberFormatException e) {
                response.put("price", "Invalid price format");
            }
        }

        // Validate stock
        if (stock == null || stock.trim().isEmpty()) {
            response.put("stock", "Stock is required");
        } else {
            try {
                Integer.parseInt(stock);
            } catch (NumberFormatException e) {
                response.put("stock", "Invalid stock quantity format");
            }
        }

        // Validate reference
        if (reference != null && reference.trim().isEmpty()) {
            response.put("reference", "Reference is optional but should not be empty if provided");
        }

        // Validate image
        // Assuming image parameter is a URL or some identifier; adjust validation as needed
        if (image == null || image.trim().isEmpty()) {
            response.put("image", "Image is required");
        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/details/update")
    public String updateMaterial(
            @RequestParam("id") Long materialId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("stock") Integer stock,
            @RequestParam(value = "ref", required = false) String ref,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("supplierId") List<Long> supplierIds,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        // Retrieve the existing material
        Material material = materialManager.getMaterialById(materialId);
        if (material == null) {
            return "redirect:/home/admin/material";
        }

        // Update material fields
        material.setName(name);
        material.setDescription(description);
        material.setPrice(price);
        material.setStock(stock);
        material.setRef(ref);

        // Update the category
        Category category = categoryManger.getCategoryById(categoryId);
        if (category != null) {
            material.setCategory(category);
        } else {
            return "redirect:/home/admin/material";
        }

        // Update the suppliers
        List<Supplier> suppliers = supplierRepo.findAllById(supplierIds);
        material.setSuppliers(suppliers);

        // Handle image update
        if (image != null && !image.isEmpty()) {
            material.setImage(image.getBytes());
        }

        // Save updated material
        materialRepo.save(material);

        Admin admin = adminManager.getAdminById(1L);
        // Notify admins and admin users about the new category creation
        String timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
        notificationManager.notifyAdminsAndAdminUsersUpdateMaterial(name,ref,timestamp,admin.getUsername());
        // Notify admins about the update


        // Redirect to the updated material details page
        return "redirect:/home/admin/material/details/" + materialId;
    }

    @PostMapping("/add/comment/{materialId}")
    @ResponseBody // This indicates that the response should be directly written to the body
    public ResponseEntity<String> addCommentAndNotify(
            @PathVariable Long materialId,
            @RequestParam String content) {

        Material material = materialManager.getMaterialById(materialId);
        Admin adminWhoMadeComment = adminManager.getAdminById(1L); // Assume the admin ID is 1L for this example

        // Create and save the comment
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setMaterial(material);
        comment.setAdmin(adminWhoMadeComment);
        comment.setPostedOn(new Date());
        commentRepo.save(comment);

        // Create the notification message
        String message = String.format(
                "Admin %s commented on material '%s' on %s",
                adminWhoMadeComment.getUsername(),
                material.getDescription(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))
        );

        // Notify all users
        List<User> usersToNotify = userRepo.findAll();
        for (User user : usersToNotify) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setDate(new Date());
            notification.setType(NotificationType.INFO);
            notification.setUser(user);
            notification.setComment(comment);
            notificationRepo.save(notification);
        }

        // Notify all admins excluding the one who made the comment
        List<Admin> adminsToNotify = adminManager.getAllAdmins();
        for (Admin admin : adminsToNotify) {
            if (!admin.equals(adminWhoMadeComment)) {
                Notification notification = new Notification();
                notification.setMessage(message);
                notification.setDate(new Date());
                notification.setType(NotificationType.INFO);
                notification.setAdmin(admin);
                notification.setComment(comment);
                notificationRepo.save(notification);
            }
        }

        return ResponseEntity.ok("Comment added and notifications sent successfully.");
    }

}

