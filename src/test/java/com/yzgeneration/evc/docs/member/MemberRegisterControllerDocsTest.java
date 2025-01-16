package com.yzgeneration.evc.docs.member;

import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.fixture.MemberFixture;
import com.yzgeneration.evc.member.controller.MemberRegisterController;
import com.yzgeneration.evc.member.service.MemberAuthenticationService;
import com.yzgeneration.evc.verification.model.EmailVerification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.yzgeneration.evc.member.dto.MemberRequest.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class MemberRegisterControllerDocsTest extends RestDocsSupport {

    private final MemberAuthenticationService memberAuthenticationService = mock(MemberAuthenticationService.class);

    @Override
    protected Object initController() {
        return new MemberRegisterController(memberAuthenticationService);
    }

    @Test
    @DisplayName("신규 회원을 생성하는 API")
    void register() throws Exception {
        EmailSignup request = MemberFixture.fixEmailSignup();
        given(memberAuthenticationService.sendEmailForVerification(any()))
                .willReturn(EmailVerification.builder()
                        .verificationCode("1234").build());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/members/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(document("member-register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING)
                                        .description("닉네임"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호"),
                                fieldWithPath("checkPassword").type(JsonFieldType.STRING)
                                        .description("비밀번호확인")
                        ),
                        responseFields(
                                fieldWithPath("verificationCode").type(JsonFieldType.STRING)
                                        .description("인증코드")
                        )));
    }
}
