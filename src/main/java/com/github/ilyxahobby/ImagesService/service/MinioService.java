package com.github.ilyxahobby.ImagesService.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MinioService {

    @Value("${minio.bucket-name}")
    private String minioBucketName;

    private final MinioClient minioClient;

    public String getObject(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(minioBucketName)
                    .object(objectName)
                    .expiry(60 * 60)
                    .build());
        }
        catch(Exception e){
            throw new RuntimeException("MINIO EXCEPTION");
        }
    }

    public String savePhoto(MultipartFile photo){
        try {
            InputStream in = new ByteArrayInputStream(photo.getBytes());
            String uuidFile = UUID.randomUUID().toString();
            String fileName = uuidFile + "-" + photo.getOriginalFilename();
            putObject(fileName, in, photo);
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("MINIO EXCEPTION");
        }
    }

    private void putObject(String objectName, InputStream inputStream, MultipartFile file) {
        try {
            minioClient.putObject(PutObjectArgs.builder().bucket(minioBucketName).object(objectName)
                    .stream(inputStream, -1, 10485760).contentType(file.getContentType()).build());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
