package softuni.bg.finalPJ.web;

import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import softuni.bg.finalPJ.models.DTOs.UserRegistrationDTO;
import softuni.bg.finalPJ.service.UserService;

@Controller
public class UserRegisterController {

    private final UserService userService;
    private Validator validator;

    @Autowired
    public UserRegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/register")
    public ModelAndView register(
            @ModelAttribute("userRegistrationDTO") UserRegistrationDTO userRegistrationDTO) {

        return new ModelAndView("auth-register");
    }

    @PostMapping("/users/register")
    public ModelAndView postRegister(
            @ModelAttribute("userRegistrationDTO")
            @Valid UserRegistrationDTO userRegistrationDTO,
            BindingResult bindingResult){


        if (bindingResult.hasErrors()){
            return new ModelAndView("auth-register");
        }

        boolean successfulRegistration = userService.register(userRegistrationDTO);

        if (!successfulRegistration){
            ModelAndView modelAndView = new ModelAndView("auth-register");
            modelAndView.addObject("hasRegistrationError", true);
            return modelAndView;
        }

        return new ModelAndView("redirect/auth-register");
    }
}
