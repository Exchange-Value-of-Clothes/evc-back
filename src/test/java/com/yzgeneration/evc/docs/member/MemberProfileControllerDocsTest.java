package com.yzgeneration.evc.docs.member;

import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.member.controller.MemberProfileController;
import com.yzgeneration.evc.domain.member.dto.ProfileResponse;
import com.yzgeneration.evc.domain.member.service.MemberProfileService;
import com.yzgeneration.evc.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberProfileControllerDocsTest extends RestDocsSupport {

    private final MemberProfileService memberProfileService = mock(MemberProfileService.class);
    @Override
    protected Object initController() {
        return new MemberProfileController(memberProfileService);
    }

    @Test
    @DisplayName("프로필을 조회한다.")
    void get() throws Exception {
        given(memberProfileService.get(any()))
                .willReturn(new ProfileResponse("imageName", "imageUrl", "nickname", 1000));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/members/profile/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageName").value("imageName"))
                .andExpect(jsonPath("$.imageUrl").value("imageUrl"))
                .andExpect(jsonPath("$.nickname").value("nickname"))
                .andExpect(jsonPath("$.point").value(1000))
                .andDo(document("profile-get",
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("imageName").type(JsonFieldType.STRING).description("이미지 이름 (presigned url 조회용)" +
                                                "imageName을 우선순위로 조회하고 null 이라면, 이미지 주소로 조회. 이것도 null이라면 프로필은 없는 상태입니다."),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("이미지 주소 (소셜 로그인에서 제공)"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("point").type(JsonFieldType.NUMBER).description("포인트")
                                )
                        )
                );

    }

    @Test
    @DisplayName("프로필을 업데이트한다.")
    void update() throws Exception {
        given(memberProfileService.update(any(), any()))
                .willReturn(new ProfileResponse("imageName", "imageUrl", "nickname", 1000));
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/members/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MemberFixture.fixtureUpdateProfileRequest("imageName", "imageUrl"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageName").value("imageName"))
                .andExpect(jsonPath("$.imageUrl").value("imageUrl"))
                .andExpect(jsonPath("$.nickname").value("nickname"))
                .andExpect(jsonPath("$.point").value(1000))
                .andDo(document("profile-update",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("imageName").type(JsonFieldType.STRING).description("이미지 이름").optional()
                                ),
                        responseFields(
                                fieldWithPath("imageName").type(JsonFieldType.STRING).description("이미지 이름"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("이미지 주소 (소셜 로그인에서 제공)"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("point").type(JsonFieldType.NUMBER).description("포인트")
                        )
                        )
                );

    }
}
