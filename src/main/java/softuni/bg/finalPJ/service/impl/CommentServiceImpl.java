package softuni.bg.finalPJ.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.bg.finalPJ.models.entities.Comment;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.CommentRepository;
import softuni.bg.finalPJ.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> getCommentsByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    @Override
    public void addComment(UserEntity user, String author, String content) {
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setAuthor(author);
        comment.setContent(content);
        comment.setCreatedDate(LocalDateTime.now());
        commentRepository.save(comment);
    }
}