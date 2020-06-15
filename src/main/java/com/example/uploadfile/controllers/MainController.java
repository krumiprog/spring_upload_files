package com.example.uploadfile.controllers;

import com.example.uploadfile.models.Image;
import com.example.uploadfile.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Controller
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/")
    public String getUploadFilePage(Model model) {
        Iterable<Image> images = imageRepository.findAll();
        model.addAttribute("images", images);

        return "gallery";
    }

    @PostMapping
    public String uploadFile(@RequestParam String description,
                             @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String resultFileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));

            Image image = new Image(resultFileName, description);
            imageRepository.save(image);
        }

        return "redirect:/";
    }
}
