package com.github.ilyxahobby.ImagesService.service;

import com.github.ilyxahobby.ImagesService.dto.ImageDto;
import com.github.ilyxahobby.ImagesService.entity.Image;
import com.github.ilyxahobby.ImagesService.mapper.ImageMapper;
import com.github.ilyxahobby.ImagesService.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final MinioService minioService;
    private final ImageMapper imageMapper;

    public List<ImageDto> getImagesByUserId(UUID userId) {
        var images = imageRepository.findByUserId(userId).stream()
                .map(this::mapImageFromMinioIdToUrl)
                .collect(Collectors.toList());
        return imageMapper.mapToImageDtos(images);
    }

    @Transactional
    public ImageDto addNewImage(UUID userId, String title, MultipartFile photo) {
        var image = Image.builder()
                .userId(userId)
                .title(title)
                .photoUrl(minioService.savePhoto(photo))
                .build();
        return imageMapper.mapToImageDto(imageRepository.saveAndFlush(image));
    }

    private Image mapImageFromMinioIdToUrl(Image image) {
        image.setPhotoUrl(minioService.getObject(image.getPhotoUrl()));
        return image;
    }
}
