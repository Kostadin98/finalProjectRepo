package softuni.bg.finalPJ.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import softuni.bg.finalPJ.models.entities.Comment;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.UserRepository;
import softuni.bg.finalPJ.service.CommentService;


import java.security.Principal;
import java.util.List;

@Controller
public class CommentController {

    private final UserRepository userRepository;
    private final CommentService commentService;

    @Autowired
    public CommentController(UserRepository userRepository, CommentService commentService) {
        this.userRepository = userRepository;
        this.commentService = commentService;
    }

//    @PostMapping("/profile/{id}/addComment")
//    public ModelAndView addComment(@PathVariable Long id,
//                                   @RequestParam String content,
//                                   @RequestParam(required = false) String author,
//                                   Principal principal) {

//        ModelAndView modelAndView = new ModelAndView("redirect:/profile/" + id);
//        UserEntity user = userRepository.findById(id).get();

//        if (principal != null) {
//            author = userRepository.findUserByEmail(principal.getName()).fullName();
//        } else if (author == null || author.isEmpty()) {
//            author = "Anonymous";
//        }
//        commentService.addComment(user, author, content);
//        return modelAndView;
//    }

    @PostMapping("profile/{id}/addComment")
    public ModelAndView addComment(@PathVariable Long id,
                                   @RequestParam String content,
                                   @RequestParam(required = false) String author,
                                   Principal principal) {

        UserEntity profileOwner = userRepository.findById(id).get();
        UserEntity currentUser = principal != null ? userRepository.findUserByEmail(principal.getName()) : null;

        if (currentUser != null && currentUser.getId().equals(id)) {
            // Prevent user from commenting on their own profile
            ModelAndView modelAndView = new ModelAndView("redirect:/profile/" + id);
            modelAndView.addObject("error", "CannotCommentOnOwnProfile");
            return modelAndView;
        }

        if (currentUser != null) {
            author = currentUser.fullName();
        } else if (author == null || author.isEmpty()) {
            author = "Anonymous";
        }

        commentService.addComment(profileOwner, author, content);

        return new ModelAndView("redirect:/profile/" + id);
    }
}
