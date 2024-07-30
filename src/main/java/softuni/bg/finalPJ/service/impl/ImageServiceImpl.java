package softuni.bg.finalPJ.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.bg.finalPJ.models.entities.Image;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.ImageRepository;
import softuni.bg.finalPJ.repositories.UserRepository;
import softuni.bg.finalPJ.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    private final String SAVE_DIRECTORY_ROUTE = "src/main/resources/static/images/";
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(UserRepository userRepository, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }


    public List<Image> findImagesByUserId(Long userId) {
        return imageRepository.findImagesByUserId(userId);
    }


    @Override
    public void saveImage(MultipartFile file, Long userId) throws IOException {
        UserEntity user = userRepository.findById(userId).get();
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        String userDirectory = SAVE_DIRECTORY_ROUTE + "user_" + userId + "/";
        Files.createDirectories(Paths.get(userDirectory));

        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(userDirectory + fileName);
        Files.write(filePath, file.getBytes());


        Image image = new Image();
        image.setFileName(fileName);

        //Correct Path to set
        String correctPath = userDirectory + fileName;
        correctPath = correctPath.replace("src/main/resources/static","");
        filePath = Paths.get(correctPath);
        image.setFilePath(filePath.toString());

        image.setFileType(file.getContentType());
        image.setUser(user);

        imageRepository.save(image);
    }
}
