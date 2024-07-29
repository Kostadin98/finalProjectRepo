package softuni.bg.finalPJ.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

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


    @GetMapping("/contact")
    public ModelAndView contact(){

        return new ModelAndView("contact");
    }
}
