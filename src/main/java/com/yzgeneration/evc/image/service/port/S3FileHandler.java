package com.yzgeneration.evc.image.service.port;

import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

public interface S3FileHandler {
    String uploadFileToS3(MultipartFile file);
    UrlResource getS3Image(String fileName) throws MalformedURLException;
    void deleteS3Image(String fileName);
}
