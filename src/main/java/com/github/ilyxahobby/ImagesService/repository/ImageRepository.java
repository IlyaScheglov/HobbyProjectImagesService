package com.github.ilyxahobby.ImagesService.repository;

import com.github.ilyxahobby.ImagesService.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {

    List<Image> findByUserId(UUID userId);
}
