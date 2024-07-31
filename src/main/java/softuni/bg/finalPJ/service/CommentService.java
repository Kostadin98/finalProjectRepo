package softuni.bg.finalPJ.service;


import softuni.bg.finalPJ.models.entities.Comment;
import softuni.bg.finalPJ.models.entities.UserEntity;

import java.util.List;

public interface CommentService {

    void addComment(UserEntity user, String author, String content);

    List<Comment> getCommentsByUserId(Long userId);

}
