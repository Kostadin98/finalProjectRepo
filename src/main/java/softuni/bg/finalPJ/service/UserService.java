package softuni.bg.finalPJ.service;

import softuni.bg.finalPJ.models.DTOs.UserRegistrationDTO;
import softuni.bg.finalPJ.models.entities.UserEntity;

import java.util.List;

public interface UserService {

    boolean register(UserRegistrationDTO userRegistrationDTO);

    List<UserEntity> searchUsers(String query);

    void updateFirstName(Long id,String firstName);

    void updateLastName(Long id, String lastName);

    void updatePassword(UserEntity user, String password);

    boolean checkIfNewAndCurrentPasswordMatches(UserEntity user, String passwordToMatch);

    boolean checkIfPasswordAndConfirmPasswordMatches(String password, String confirmPassword);
}
