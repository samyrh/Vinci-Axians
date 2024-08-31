package vinci.ma.inventory.services.managers;

import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vinci.ma.inventory.dao.entities.Category;

import java.io.IOException;
import java.util.List;

public interface CategoryManager {

    Category createCategory(Category category);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();


    @Transactional
    void deleteCategory(Long id);

    boolean isCategoryNameTaken(String name);
    void updateCategory(Long id, String name, String description, MultipartFile categoryPicture) throws IOException;

}
