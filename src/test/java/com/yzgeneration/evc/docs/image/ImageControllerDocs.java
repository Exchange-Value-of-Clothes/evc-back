package com.yzgeneration.evc.docs.image;

import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.image.controller.ImageController;
import com.yzgeneration.evc.domain.image.dto.ImageResponse;
import com.yzgeneration.evc.domain.image.service.ImageService;
import com.yzgeneration.evc.mock.usedItem.MockUsedItemImageFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ImageControllerDocs extends RestDocsSupport {
    private final ImageService imageService = mock(ImageService.class);

    @Override
    protected Object initController() {
        return new ImageController(imageService);
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

        when(imageService.generatePresignedURL(anyString(), anyList())).thenReturn(List.of(imageResponse1, imageResponse2));

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
}
