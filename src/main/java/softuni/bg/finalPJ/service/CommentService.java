package softuni.bg.finalPJ.service;


import softuni.bg.finalPJ.models.DTOs.CommentDTO;
import softuni.bg.finalPJ.models.entities.Comment;
import softuni.bg.finalPJ.models.entities.UserEntity;

import java.util.List;

public interface CommentService {

    void addComment(CommentDTO commentDTO);

    List<Comment> getCommentsByUserId(Long userId);

    void deleteComment(Long commentId);

    Comment findById (Long commentId);
}
