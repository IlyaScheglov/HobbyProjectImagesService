package com.github.ilyxahobby.ImagesService.mapper;

import com.github.ilyxahobby.ImagesService.dto.ImageDto;
import com.github.ilyxahobby.ImagesService.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImageMapper {


    @Mapping(target = "imageName", source = "title")
    @Mapping(target = "imageUrl", source = "photoUrl")
    ImageDto mapToImageDto(Image image);

    default List<ImageDto> mapToImageDtos(List<Image> images) {
        return images.stream()
                .map(this::mapToImageDto)
                .collect(Collectors.toList());
    }
}
