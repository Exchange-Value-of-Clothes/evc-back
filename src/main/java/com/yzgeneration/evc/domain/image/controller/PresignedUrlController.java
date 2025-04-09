package com.yzgeneration.evc.domain.image.controller;

import com.yzgeneration.evc.domain.image.dto.ImageRequest;
import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.service.PresignedUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class PresignedUrlController {
    private final PresignedUrlService presignedUrlService;

    @PostMapping
    public List<ImageResponse> createForItem(@RequestBody ImageRequest imageRequest) {
        return presignedUrlService.generateForItem(imageRequest);
    }

    @PostMapping("/profile")
    public ImageResponse createForProfile(@RequestParam("imageName") String imageName) {
        return presignedUrlService.generateForProfile(imageName);
    }
}
