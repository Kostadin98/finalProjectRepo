package softuni.bg.finalPJ.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import softuni.bg.finalPJ.models.DTOs.UserRegistrationDTO;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.models.entities.UserRoleEntity;
import softuni.bg.finalPJ.models.enums.UserRoleEnum;
import softuni.bg.finalPJ.repositories.UserRepository;
import softuni.bg.finalPJ.repositories.UserRoleRepository;
import softuni.bg.finalPJ.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean register(UserRegistrationDTO userRegistrationDTO) {

        boolean userAlreadyExists = userRepository.findUserEntityByEmail(userRegistrationDTO.getEmail()).isPresent();
        boolean passwordMatches = userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword());

        if (userAlreadyExists || !passwordMatches){
            return false;
        }

        UserRoleEntity roleToSet = userRoleRepository.findUserRoleEntityByRole(UserRoleEnum.NORMAL).get();

        UserEntity userToSave = new UserEntity();
        userToSave.setEmail(userRegistrationDTO.getEmail());
        userToSave.setFistName(userRegistrationDTO.getFirstName());
        userToSave.setLastName(userRegistrationDTO.getLastName());
        userToSave.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        userToSave.addRole(roleToSet);

        userRepository.save(userToSave);
        return true;
    }
}
