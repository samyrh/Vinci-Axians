package vinci.ma.inventory.services.managers;

import vinci.ma.inventory.dao.entities.Comment;

import java.util.List;

public interface CommentManager {

    Comment createComment(Comment comment);
    Comment getCommentById(Long id);
    List<Comment> getAllComments();
    void deleteComment(Long id);
}
