package com.yzgeneration.evc.image;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.domain.image.controller.PresignedUrlController;
import com.yzgeneration.evc.domain.image.dto.ImageRequest;
import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.service.PresignedUrlService;
import com.yzgeneration.evc.mock.WithFakeUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

import static com.yzgeneration.evc.fixture.ImageFixture.fixImageRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    @DisplayName("중고상품 및 경매상품을 위한 presignedURL")
    void createForItem() throws Exception {

        ImageRequest imageRequest = fixImageRequest();

        when(presignedUrlService.generatePresignedURL(any()))
                .thenReturn(List.of(ImageResponse.builder().presignedURL("https://www.abc.com").imageName("imageName.jpg").build()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/images")
                        .content(objectMapper.writeValueAsString(imageRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].presignedURL").value("https://www.abc.com"))
                .andExpect(jsonPath("$[0].imageName").value("imageName.jpg"));
    }

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
