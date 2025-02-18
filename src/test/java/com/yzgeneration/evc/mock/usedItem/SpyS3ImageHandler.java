package com.yzgeneration.evc.mock.usedItem;

import com.yzgeneration.evc.image.service.port.S3FileHandler;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URL;

public class SpyS3ImageHandler implements S3FileHandler {
    @Override
    public String uploadFileToS3(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return "http://test-s3-bucket/" + fileName;
    }

    @Override
    public UrlResource getS3Image(String fileName) throws MalformedURLException {
        return new UrlResource(new URL("http://test-s3-bucket/" + fileName));
    }

    @Override
    public void deleteS3Image(String fileName) { }
}
