package softuni.bg.finalPJ.web;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import softuni.bg.finalPJ.models.entities.Image;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.ImageRepository;
import softuni.bg.finalPJ.repositories.UserRepository;
import softuni.bg.finalPJ.service.ImageService;
import softuni.bg.finalPJ.service.QrCodeService;
import softuni.bg.finalPJ.service.UserService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final QrCodeService qrCodeService;


    @Autowired
    public UserController(UserRepository userRepository, UserService userService, ImageRepository imageRepository, ImageService imageService, QrCodeService qrCodeService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.imageRepository = imageRepository;
        this.imageService = imageService;
        this.qrCodeService = qrCodeService;
    }


    @GetMapping("/profile")
    public ModelAndView showLoggedInUserProfile(Authentication authentication) throws IOException, WriterException {
        UserEntity user = userRepository.findUserByEmail(authentication.getName());

        if (user.getQrCodePath() == null || user.getQrCodePath().isEmpty()) {
            String qrCodePath = qrCodeService.generateQRCodeImage("http://localhost:8080/profile/" + user.getId(), user.getId());
            user.setQrCodePath(qrCodePath);
            userRepository.save(user);
        }

        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/profile/{id}")
    public ModelAndView showUserProfile(@PathVariable("id") Long id) throws IOException, WriterException {
        ModelAndView modelAndView = new ModelAndView();
        UserEntity user = userRepository.findById(id).orElseThrow(null);
        if (user == null) {
            modelAndView.setViewName("error/404");
        } else {
            if (user.getQrCodePath() == null || user.getQrCodePath().isEmpty()) {
                String qrCodePath = qrCodeService.generateQRCodeImage("http://localhost:8080/profile/" + id, id);
                user.setQrCodePath(qrCodePath);
                userRepository.save(user);
            }
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

        List<Image> images = imageRepository.findImagesByUserId(id);
        ModelAndView modelAndView = new ModelAndView("gallery");
        modelAndView.addObject("user", user);
        modelAndView.addObject("images", images);
        return modelAndView;
    }

    @PostMapping("/profile/{id}/gallery/upload")
    public ModelAndView uploadImage(@PathVariable("id") Long id,
                                    @RequestParam("file") MultipartFile file,
                                    Principal principal) {
        UserEntity user = userRepository.findById(id).get();
        if (user == null || !user.getEmail().equals(principal.getName())) {
            return new ModelAndView("error/403"); // Access Denied or User Not Found
        }

        try {
            imageService.saveImage(file, id);
        } catch (IOException e) {
            e.printStackTrace();
            return new ModelAndView("error/500"); // Internal Server Error
        }

        return new ModelAndView("redirect:/profile/" + id + "/gallery");
    }






    @PostMapping("/profile/{id}/uploadAvatar")
    public ModelAndView uploadAvatar(@PathVariable("id") Long id,
                                    @RequestParam("file") MultipartFile file,
                                    Principal principal) {

        UserEntity user = userRepository.findById(id).get();
        if (user == null || !user.getEmail().equals(principal.getName())) {
            return new ModelAndView("error/403"); // Access Denied or User Not Found
        }

        try {
            imageService.saveAvatarImage(file, id);
        } catch (IOException e) {
            e.printStackTrace();
            return new ModelAndView("error/500"); // Internal Server Error
        }

        return new ModelAndView("redirect:/profile/" + id);
    }
}

