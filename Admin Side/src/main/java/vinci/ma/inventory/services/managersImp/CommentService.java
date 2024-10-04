package vinci.ma.inventory.services.managersImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vinci.ma.inventory.dao.entities.Comment;
import vinci.ma.inventory.dao.repositories.CommentRepo;
import vinci.ma.inventory.services.managers.CommentManager;

import java.util.List;

@Service
public class CommentService implements CommentManager {

    @Autowired
    private CommentRepo commentRepository;

    @Override
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
