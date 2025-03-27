package com.yzgeneration.evc.docs.image;

import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.image.controller.PresignedUrlController;
import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.service.PresignedUrlService;

import com.yzgeneration.evc.mock.image.MockUsedItemImageFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
    @DisplayName("이미지 저장을 위한 presigned url 생성")
    void createPresignedURL() throws Exception {
        ImageResponse imageResponse1 = ImageResponse.builder()
                .presignedURL("http://localhost:8080/image/1234.jpg")
                .imageName("1234.jpg")
                .build();
        ImageResponse imageResponse2 = ImageResponse.builder()
                .presignedURL("http://localhost:8080/image/5678.jpg")
                .imageName("5678.jpg")
                .build();

        when(presignedUrlService.generatePresignedURL(anyString(), anyList())).thenReturn(List.of(imageResponse1, imageResponse2));

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/api/images")
                        .file("imageFiles", new MockUsedItemImageFile().getImageFiles().get(0).getBytes())
                        .file("imageFiles", new MockUsedItemImageFile().getImageFiles().get(1).getBytes())
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .queryParam("prefix", "prefix-name"))
                .andExpect(status().isOk())
                .andDo(document("image-presignedURL-create",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(parameterWithName("prefix").description("이미지 경로 (useditem, auction, chat)")),
                                requestParts(partWithName("imageFiles").description("업로드할 이미지 list")),
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
