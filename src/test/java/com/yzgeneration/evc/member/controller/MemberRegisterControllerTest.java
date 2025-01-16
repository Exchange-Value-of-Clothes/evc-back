package com.yzgeneration.evc.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.yzgeneration.evc.fixture.MemberFixture;
import com.yzgeneration.evc.member.service.MemberAuthenticationService;
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
    private MemberAuthenticationService memberAuthenticationService;

    @Test
    @WithMockUser
    @DisplayName("신규 회원을 생성한다.")
    void register() throws Exception {
        EmailSignup request = MemberFixture.fixEmailSignup();
        given(memberAuthenticationService.sendEmailForVerification(any()))
                .willReturn(EmailVerification.builder()
                        .verificationCode("1234").build());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/members/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}