package com.github.ilyxahobby.ImagesService.controller;

import com.github.ilyxahobby.ImagesService.dto.ImageDto;
import com.github.ilyxahobby.ImagesService.service.ImageService;
import com.github.ilyxahobby.ImagesService.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.V1_API_MAPPING + "/my-photos")
public class MyImagesController {

    private final ImageService imageService;

    @GetMapping("/{userId}")
    public List<ImageDto> getMyImages(@PathVariable UUID userId) {
        return imageService.getImagesByUserId(userId);
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ImageDto addNewPhoto(@RequestParam String title, @RequestParam UUID userId, @RequestPart MultipartFile photo) {
        return imageService.addNewImage(userId, title, photo);
    }
}
