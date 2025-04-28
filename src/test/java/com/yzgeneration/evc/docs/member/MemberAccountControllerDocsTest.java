package com.yzgeneration.evc.docs.member;

import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.member.controller.MemberAccountController;
import com.yzgeneration.evc.domain.member.service.MemberAccountService;
import com.yzgeneration.evc.fixture.MemberFixture;
import com.yzgeneration.evc.mock.WithFakeUser;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberAccountControllerDocsTest extends RestDocsSupport {

    private final MemberAccountService memberAccountService = mock(MemberAccountService.class);

    @Override
    protected Object initController() {
        return new MemberAccountController(memberAccountService);
    }

    @Test
    @DisplayName("이메일을 변경할 수 있다.")
    void changeEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/members/account/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MemberFixture.fixtureChangeEmail())))
                .andExpect(status().isOk())
                .andDo(document("email-change",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("변경할 이메일")),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부")
                        )));
    }

    @Test
    @DisplayName("비밀번호를 변경할 수 있다.")
    void changePassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/members/account/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MemberFixture.fixtureChangePassword())))
                .andExpect(status().isOk())
                .andDo(document("password-change",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("oldPassword").type(JsonFieldType.STRING).description("기존 비밀번호"),
                                fieldWithPath("newPassword").type(JsonFieldType.STRING).description("새 비밀번호"),
                                fieldWithPath("checkPassword").type(JsonFieldType.STRING).description("새 비밀번호 확인")),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부")
                        )));
    }

    @Test
    @DisplayName("개인정보를 조회할 수 있다.")
    void getPrivateInfo() throws Exception {
        given(memberAccountService.getPrivateInfo(any()))
                .willReturn(MemberFixture.fixtureMemberPrivateInformation("accountName", "12345678", "01012345678"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/members/account")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("accountName").value("accountName"))
                .andExpect(jsonPath("accountNumber").value("12345678"))
                .andExpect(jsonPath("phoneNumber").value("01012345678"))
                .andExpect(jsonPath("basicAddress").value("basicAddress"))
                .andExpect(jsonPath("detailAddress").value("detailAddress"))
                .andExpect(jsonPath("latitude").value(0.0))
                .andExpect(jsonPath("longitude").value(0.0))
                .andDo(document("get-privateInfo",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("accountName").type(JsonFieldType.STRING).description("계좌주"),
                                fieldWithPath("accountNumber").type(JsonFieldType.STRING).description("계좌번호"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
                                fieldWithPath("basicAddress").type(JsonFieldType.STRING).description("기본주소"),
                                fieldWithPath("detailAddress").type(JsonFieldType.STRING).description("상세주소"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도")
                        )));
    }

    @Test
    @DisplayName("개인정보를 수정할 수 있다.")
    void changePrivateInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/members/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MemberFixture.fixtureMemberPrivateInformationUpdate("accountName","12345678", "01012345678"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andDo(document("change-privateInfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("accountName").type(JsonFieldType.STRING).description("계좌명"),
                                fieldWithPath("accountNumber").type(JsonFieldType.STRING).description("계좌번호"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부")
                        )));
    }
}
