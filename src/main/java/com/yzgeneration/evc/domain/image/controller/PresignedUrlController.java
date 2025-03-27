package com.yzgeneration.evc.domain.image.controller;

import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.service.PresignedUrlProvider;
import com.yzgeneration.evc.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class PresignedUrlController {
    private final PresignedUrlProvider presignedUrlProvider;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<ImageResponse> createPresignedURL(@RequestParam String prefix, @RequestPart List<MultipartFile> imageFiles) {
        return presignedUrlProvider.generatePresignedURL(prefix, imageFiles);
    }

    @PostMapping(value = "/profile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageResponse createForProfile(@RequestParam String fileName) {
        return presignedUrlProvider.generateForProfile(fileName);
    }
}
