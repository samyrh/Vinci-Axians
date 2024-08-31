package vinci.ma.inventory.services.managersImp;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vinci.ma.inventory.dao.entities.Admin;
import vinci.ma.inventory.dao.entities.Category;
import vinci.ma.inventory.dao.entities.Material;
import vinci.ma.inventory.dao.entities.User;
import vinci.ma.inventory.dao.repositories.AdminRepo;
import vinci.ma.inventory.dao.repositories.CategoryRepo;
import vinci.ma.inventory.dao.repositories.MaterialRepo;
import vinci.ma.inventory.dao.repositories.UserRepo;
import vinci.ma.inventory.services.managers.CategoryManager;

import java.io.IOException;
import java.util.List;

@Service
public class CategoryService implements CategoryManager {

    @Autowired
    private CategoryRepo categoryRepository;
    @Autowired
    private MaterialRepo materialRepository;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private UserRepo userRepo;


    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        // Find the category
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new EntityNotFoundException("Category not found");
        }

        // Remove category reference from associated materials
        for (Material material : category.getMaterials()) {
            material.setCategory(null); // Clear the reference
            materialRepository.save(material); // Save changes using materialRepo

            // Also ensure to handle references in other tables
            for (Admin admin : material.getAdmins()) {
                admin.getMaterials().remove(material); // Remove material reference from Admin
                adminRepo.save(admin);
            }
            // Also ensure to handle references in other tables
            for (User user : material.getUsers()) {
                user.getMaterials().remove(material); // Remove material reference from Admin
                userRepo.save(user);
            }
        }

        // Now delete the category
        categoryRepository.deleteById(id);

    }
    @Override
    public boolean isCategoryNameTaken(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public void updateCategory(Long id, String name, String description, MultipartFile categoryPicture) throws IOException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        category.setName(name);
        category.setDescription(description);

        // Update picture only if a new one is uploaded
        if (categoryPicture != null && !categoryPicture.isEmpty()) {
            category.setCategoryPicture(categoryPicture.getBytes());
        }

        categoryRepository.save(category);
    }

}
