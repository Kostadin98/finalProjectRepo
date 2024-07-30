package softuni.bg.finalPJ.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.UserRepository;
import softuni.bg.finalPJ.service.UserService;

@Controller
public class UserSettingsController {

        private final UserService userService;
        private final UserRepository userRepository;

        @Autowired
        public UserSettingsController(UserService userService, UserRepository userRepository) {
            this.userService = userService;
            this.userRepository = userRepository;
        }

        @GetMapping("/profile/{id}/settings")
        public ModelAndView showSettings(@PathVariable Long id) {
            UserEntity user = userRepository.findById(id).get();
            ModelAndView mav = new ModelAndView("settings");
            mav.addObject("user", user);
            return mav;
        }

        @PostMapping("/profile/{id}/settings/updateFirstName")
        public ModelAndView updateFirstName(@PathVariable Long id, @RequestParam String firstName) {

            userService.updateFirstName(id, firstName);

            return new ModelAndView("redirect:/profile/" + id);
        }

        @PostMapping("/profile/{id}/settings/updateLastName")
        public ModelAndView updateLastName(@PathVariable Long id, @RequestParam String lastName) {

            userService.updateLastName(id, lastName);
            return new ModelAndView("redirect:/profile/" + id);
        }

        @PostMapping("/profile/{id}/settings/updatePassword")
        public ModelAndView updatePassword(@PathVariable Long id,
                                           @RequestParam String password,
                                           @RequestParam String confirmPassword,
                                           @RequestParam String currentPassword) {

            UserEntity user = userRepository.findById(id).get();
            boolean passwordMatches = userService.checkIfNewAndCurrentPasswordMatches(user, currentPassword);
            boolean confirmPasswordMatches = userService.checkIfPasswordAndConfirmPasswordMatches(password, confirmPassword);

            if (!passwordMatches){

                ModelAndView modelAndView = new ModelAndView("settings");
                modelAndView.addObject("user", user);
                modelAndView.addObject("currentPasswordError", "Current password is incorrect.");
                return modelAndView;
            }

           if (!confirmPasswordMatches) {

               ModelAndView modelAndView = new ModelAndView("settings");
               modelAndView.addObject("user", user);
               modelAndView.addObject("confirmPasswordError", "Passwords do not match.");
               return modelAndView;
           }

           userService.updatePassword(user, password);

            return new ModelAndView("redirect:/profile/" + id);

       }

    }
