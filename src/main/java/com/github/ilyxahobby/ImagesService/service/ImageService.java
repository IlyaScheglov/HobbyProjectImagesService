package com.github.ilyxahobby.ImagesService.service;

import com.github.ilyxahobby.ImagesService.dto.ImageDto;
import com.github.ilyxahobby.ImagesService.entity.Image;
import com.github.ilyxahobby.ImagesService.mapper.ImageMapper;
import com.github.ilyxahobby.ImagesService.repository.ImageRepository;
import io.github.ilyascheglov.service.MinioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final MinioService minioService;
    private final ImageMapper imageMapper;

    public List<ImageDto> getImagesByUserId(UUID userId) {
        return imageMapper.mapToImageDtos(imageRepository.findByUserId(userId));
    }

    @Transactional
    public ImageDto addNewImage(UUID userId, String title, MultipartFile photo) {
        try (InputStream inputStream = photo.getInputStream()) {
            var image = Image.builder()
                    .userId(userId)
                    .title(title)
                    .photoUrl(minioService.uploadFile(photo.getName(), photo.getContentType(), inputStream))
                    .build();
            return imageMapper.mapToImageDto(imageRepository.saveAndFlush(image));
        } catch (IOException e) {
            throw new RuntimeException("500 minio saving error");
        }
    }
}
