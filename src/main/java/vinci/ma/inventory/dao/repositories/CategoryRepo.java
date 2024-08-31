package vinci.ma.inventory.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vinci.ma.inventory.dao.entities.Category;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    // Custom query methods (if any)

    boolean  existsByName(String name);

    List<Category> findByNameContaining(String name);
}