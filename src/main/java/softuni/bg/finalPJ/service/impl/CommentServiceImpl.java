package softuni.bg.finalPJ.service.impl;

import org.hibernate.annotations.NotFound;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.bg.finalPJ.models.DTOs.CommentDTO;
import softuni.bg.finalPJ.models.entities.Comment;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.CommentRepository;
import softuni.bg.finalPJ.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Comment> getCommentsByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = this.findById(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public void addComment(CommentDTO commentDTO) {

        commentDTO.setCreatedDate(LocalDateTime.now());

        Comment comment = this.modelMapper.map(commentDTO, Comment.class);
        commentRepository.save(comment);
    }

    @Override
    public Comment findById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->  new IllegalArgumentException("Comment not found"));

        return comment;
    }


}
