package com.yzgeneration.evc.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.yzgeneration.evc.fixture.MemberFixture;
import com.yzgeneration.evc.member.service.MemberRegisterService;
import com.yzgeneration.evc.verification.model.EmailVerification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.yzgeneration.evc.member.dto.MemberRequest.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = MemberRegisterController.class)
class MemberRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MemberRegisterService memberRegisterService;

    @Test
    @WithMockUser
    @DisplayName("신규 회원을 생성한다.")
    void register() throws Exception {
        EmailSignup request = MemberFixture.fixEmailSignup();
        given(memberRegisterService.sendEmailForRequestVerification(any()))
                .willReturn(EmailVerification.builder()
                        .verificationCode("1234").build());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/members/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("인증코드를 통해 인증한다.")
    void verify() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/members/register/verify")
                        .param("code", "1234")
                        .with(csrf()))
                    .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("인증코드를 요청한다.")
    void resendVerificationCode() throws Exception {
        given(memberRegisterService.resendVerificationCode(any()))
                .willReturn("1234");
        mockMvc.perform(MockMvcRequestBuilders.get("/api/members/register/request-code")
                        .param("email", "ssar@naver.com")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}