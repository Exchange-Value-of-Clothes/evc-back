package com.yzgeneration.evc.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.yzgeneration.evc.image.service.port.S3FileHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class S3ImageHandler implements S3FileHandler {

    private final AmazonS3 amazonS3;

    @Value("${cloude.aws.s3.bucket}")
    private String bucket;

    @Override
    public String uploadFileToS3(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(bucket, fileName, inputStream, metadata);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + fileName, e);
        }

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    @Override
    public UrlResource getS3Image(String fileName) {
        return new UrlResource(amazonS3.getUrl(bucket, fileName));
    }

    @Override
    public void deleteS3Image(String fileName) {
        amazonS3.deleteObject(bucket, fileName);
    }
}
