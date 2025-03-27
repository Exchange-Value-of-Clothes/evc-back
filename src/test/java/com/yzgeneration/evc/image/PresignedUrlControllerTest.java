package com.yzgeneration.evc.image;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.domain.image.controller.PresignedUrlController;
import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.service.PresignedUrlService;
import com.yzgeneration.evc.mock.WithFakeUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PresignedUrlController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
        })
class PresignedUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PresignedUrlService presignedUrlService;

    @Test
    @WithFakeUser
    @DisplayName("프로필사진을 위한 presignedUrl을 생성한다.")
    void createForProfile() throws Exception {
        given(presignedUrlService.generateForProfile(any()))
                .willReturn(ImageResponse.builder().presignedURL("https://www.abc.com").imageName("profile/abc-abc.jpg").build());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/images/profile")
                .queryParam("imageName", "profile/abc-abc.jpg")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.presignedURL").value("https://www.abc.com"))
                .andExpect(jsonPath("$.imageName").value("profile/abc-abc.jpg"));
    }
}
