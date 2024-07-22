package softuni.bg.finalPJ.service;

import softuni.bg.finalPJ.models.DTOs.UserRegistrationDTO;

public interface UserService {

    boolean register(UserRegistrationDTO userRegistrationDTO);
}
