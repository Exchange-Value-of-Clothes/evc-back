package com.yzgeneration.evc.domain.image.controller;

import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.service.PresignedUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class PresignedUrlController {
    private final PresignedUrlService presignedUrlService;

    @PostMapping
    public List<ImageResponse> createPresignedURL(@RequestParam String prefix, @RequestPart List<String> imageNames) {
        return presignedUrlService.generatePresignedURL(prefix, imageNames);
    }

    @PostMapping("/profile")
    public ImageResponse createForProfile(@RequestParam("imageName") String imageName) {
        return presignedUrlService.generateForProfile(imageName);
    }
}
