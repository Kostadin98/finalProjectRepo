package softuni.bg.finalPJ.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import softuni.bg.finalPJ.models.entities.Message;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.UserRepository;
import softuni.bg.finalPJ.service.MessageService;

import java.security.Principal;

@Controller
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;

    @Autowired
    public MessageController(MessageService messageService, UserRepository userRepository) {
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile/{id}/contactForm")
    public ModelAndView showContactForm(@PathVariable Long id) {

        ModelAndView modelAndView = new ModelAndView("contactForm");

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/profile/{id}/submitContactForm")
    public ModelAndView submitContactForm(@PathVariable Long id,
                                          @RequestParam String name,
                                          @RequestParam String email,
                                          @RequestParam String message) {

        Message newMessage = new Message();
        newMessage.setSenderName(name);
        newMessage.setSenderEmail(email);
        newMessage.setContent(message);

        messageService.saveMessage(newMessage, id);

        ModelAndView modelAndView = new ModelAndView("redirect:/profile/" + id);
        return modelAndView;
    }

    @GetMapping("/profile/{id}/messages/inbox")
    @Secured("ROLE_USER")
    public ModelAndView showInbox(@PathVariable Long id,
                                  Principal principal) {


        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getEmail().equals(principal.getName())) {
            throw new AccessDeniedException("You are not authorized to view this page");
        }

        ModelAndView modelAndView = new ModelAndView("inbox");
        modelAndView.addObject("messages", messageService.getMessagesForUser(id));
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @Secured("ROLE_USER")
    @PostMapping("/profile/{id}/messages/deleteMessage")
    public ModelAndView deleteMessage(@PathVariable Long id,
                                      @RequestParam Long messageId){

        messageService.deleteMessage(messageId, id);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ModelAndView modelAndView = new ModelAndView("inbox");
        modelAndView.addObject("messages", messageService.getMessagesForUser(id));
        modelAndView.addObject("user", user);

        return modelAndView;
    }

}

