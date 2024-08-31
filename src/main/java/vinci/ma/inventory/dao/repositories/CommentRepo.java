package vinci.ma.inventory.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vinci.ma.inventory.dao.entities.Comment;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    // Custom query methods (if any)

    List<Comment> findAllByMaterialId(Long materialId);
}