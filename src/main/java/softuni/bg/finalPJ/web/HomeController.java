package softuni.bg.finalPJ.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.service.UserService;

import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/search")
    public ModelAndView listUsers(@RequestParam(value = "query", required = false) String query) {
        List<UserEntity> users;
        if (query != null && !query.isEmpty()) {
            users = userService.searchUsers(query);
        } else {
            users = userService.findAllUsers();
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("search");
        modelAndView.addObject("users", users);
        modelAndView.addObject("query", query);
        return modelAndView;
    }


    @GetMapping("/")
    public ModelAndView index(){

        return new ModelAndView("index");
    }

    @GetMapping("/home")
    public ModelAndView home(){

        return new ModelAndView("home");
    }

    @GetMapping("/about")
    public ModelAndView about(){

        return new ModelAndView("about");
    }


}
