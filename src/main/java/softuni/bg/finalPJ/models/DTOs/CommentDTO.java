package softuni.bg.finalPJ.models.DTOs;

import org.modelmapper.ModelMapper;
import softuni.bg.finalPJ.models.entities.UserEntity;

import java.time.LocalDateTime;

public class CommentDTO {

    private String content;
    private LocalDateTime createdDate;
    private String author;
    private UserEntity user;

    public CommentDTO() {
    }

    public String getContent() {
        return content;
    }

    public CommentDTO setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public CommentDTO setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public CommentDTO setAuthor(String author) {
        this.author = author;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public CommentDTO setUser(UserEntity user) {
        this.user = user;
        return this;
    }
}
