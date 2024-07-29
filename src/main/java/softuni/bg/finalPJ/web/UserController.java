package softuni.bg.finalPJ.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.UserRepository;
import softuni.bg.finalPJ.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;


    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @GetMapping("/profile")
    public ModelAndView profile(Principal principal) {
        String email = principal.getName(); // Assuming the principal contains the email
        UserEntity user = userRepository.findUserByEmail(email);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("/profile/{id}")
    public ModelAndView profile(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        UserEntity user = userRepository.findById(id).orElseThrow(null);
        if (user == null) {
            modelAndView.setViewName("error/404");
        } else {
            modelAndView.setViewName("profile");
            modelAndView.addObject("user", user);
        }
        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView listUsers(@RequestParam(value = "query", required = false) String query) {
        List<UserEntity> users;
        if (query != null && !query.isEmpty()) {
            users = userService.searchUsers(query);
        } else {
            users = userRepository.findAll();
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("search");
        modelAndView.addObject("users", users);
        modelAndView.addObject("query", query);
        return modelAndView;
    }

    @GetMapping("/profile/{id}/gallery")
    public ModelAndView viewGallery(@PathVariable("id") Long id, Principal principal) {
        UserEntity user = userRepository.findById(id).orElseThrow(null);
        if (user == null) {
            return new ModelAndView("error/404"); // User Not Found
        }

//        List<Image> images = userService.findImagesByUserId(id);
        ModelAndView modelAndView = new ModelAndView("gallery");
        modelAndView.addObject("user", user);
//        modelAndView.addObject("images", images);
        return modelAndView;
    }
}

