package com.yzgeneration.evc.image;

import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.service.PresignedUrlService;
import com.yzgeneration.evc.mock.image.SpyImageHandler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PresignedUrlServiceTest {

    private PresignedUrlService presignedUrlService;

    @BeforeEach
    void init() {
        presignedUrlService = new PresignedUrlService(new SpyImageHandler());
    }

    @Test
    @DisplayName("프로필사진을 위한 presignedUrl을 생성할 수 있다.")
    void generateForProfile() {
        // given
        String fileName = "test.jpg";
        // when
        ImageResponse imageResponse = presignedUrlService.generateForProfile(fileName);

        // then
        assertThat(imageResponse.getPresignedURL()).isNotNull();
        assertThat(imageResponse.getImageName()).startsWith("profile");
        assertThat(imageResponse.getImageName()).endsWith(fileName);
    }

}