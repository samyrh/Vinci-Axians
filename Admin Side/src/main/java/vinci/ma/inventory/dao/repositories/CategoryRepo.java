package vinci.ma.inventory.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vinci.ma.inventory.dao.entities.Category;
import vinci.ma.inventory.dto.CategoryDtoChart;

import java.util.List;
import java.util.Map;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    // Custom query methods (if any)

    boolean  existsByName(String name);
    // CategoryRepository
    @Query("SELECT new map(c.name as name, COUNT(m) as materialCount) FROM Category c LEFT JOIN c.materials m GROUP BY c.name")
    List<Map<String, Object>> getCategoryData();
    List<Category> findByNameContaining(String name);
}