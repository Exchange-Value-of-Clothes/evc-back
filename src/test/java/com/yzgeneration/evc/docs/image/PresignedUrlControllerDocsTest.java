package com.yzgeneration.evc.docs.image;

import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.image.controller.PresignedUrlController;
import com.yzgeneration.evc.domain.image.dto.ImageRequest;
import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.service.PresignedUrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.yzgeneration.evc.fixture.ImageFixture.fixImageRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PresignedUrlControllerDocsTest extends RestDocsSupport {
    private final PresignedUrlService presignedUrlService = mock(PresignedUrlService.class);


    @Override
    protected Object initController() {
        return new PresignedUrlController(presignedUrlService);
    }

    @Test
    @DisplayName("아이템 이미지 저장을 위한 presigned url 생성")
    void createForItem() throws Exception {

        ImageRequest imageRequest = fixImageRequest();
        ImageResponse imageResponse = ImageResponse.builder()
                .presignedURL("http://localhost:8080/presignedURL")
                .imageName("imageName.jpg")
                .build();

        when(presignedUrlService.generatePresignedURL(any()))
                .thenReturn(List.of(imageResponse));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/images")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(imageRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("image-presignedURL-create",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("prefix").type(JsonFieldType.STRING).description("이미지 경로 (useditem, auction, chat)"),
                                        fieldWithPath("imageNames").type(JsonFieldType.ARRAY).description("업로드할 이미지 이름 list")
                                ),
                                responseFields(
                                        fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                                .description("생성된 presigned url 리스트"),
                                        fieldWithPath("[].presignedURL").type(JsonFieldType.STRING)
                                                .description("이미지 저장을 위한 presigned url"),
                                        fieldWithPath("[].imageName").type(JsonFieldType.STRING)
                                                .description("이미지 이름")
                                )
                        )
                );
    }

    @Test
    @DisplayName("프로필사진을 위한 presignedUrl을 생성한다.")
    void createForProfile() throws Exception {
        given(presignedUrlService.generateForProfile(any()))
                .willReturn(ImageResponse.builder().presignedURL("https://www.abc.com").imageName("profile/abc-abc.jpg").build());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/images/profile")
                        .queryParam("imageName", "profile/abc-abc.jpg"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.presignedURL").value("https://www.abc.com"))
                .andExpect(jsonPath("$.imageName").value("profile/abc-abc.jpg"))
                .andDo(document("profile-image-presignedURL-create",
                                preprocessResponse(prettyPrint()),
                                queryParameters(parameterWithName("imageName").description("이미지 이름")),
                                responseFields(
                                        fieldWithPath("presignedURL").type(JsonFieldType.STRING).description("presigned URL"),
                                        fieldWithPath("imageName").type(JsonFieldType.STRING).description("이미지 이름")
                                )
                        )
                );
    }
}
