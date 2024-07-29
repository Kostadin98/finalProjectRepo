package softuni.bg.finalPJ.service;

import org.springframework.security.core.userdetails.User;
import softuni.bg.finalPJ.models.DTOs.UserRegistrationDTO;
import softuni.bg.finalPJ.models.entities.UserEntity;

import java.util.List;

public interface UserService {

    boolean register(UserRegistrationDTO userRegistrationDTO);

    List<UserEntity> searchUsers(String query);
}
