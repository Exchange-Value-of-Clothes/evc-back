package com.yzgeneration.evc.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.domain.member.controller.MemberAccountController;
import com.yzgeneration.evc.domain.member.service.MemberAccountService;
import com.yzgeneration.evc.fixture.MemberFixture;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberAccountController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
        })
public class MemberAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MemberAccountService memberAccountService;

    @Test
    @WithFakeUser
    @DisplayName("이메일을 변경할 수 있다.")
    void changeEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/members/account/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MemberFixture.fixtureChangeEmail()))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithFakeUser
    @DisplayName("비밀번호를 변경할 수 있다.")
    void changePassword() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/members/account/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MemberFixture.fixtureChangePassword()))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithFakeUser
    @DisplayName("개인정보를 조회할 수 있다.")
    void getPrivateInfo() throws Exception {
        given(memberAccountService.getPrivateInfo(any()))
                .willReturn(MemberFixture.fixtureMemberPrivateInformation("accountName", "12345678", "01012345678"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/members/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("accountName").value("accountName"))
                .andExpect(jsonPath("accountNumber").value("12345678"))
                .andExpect(jsonPath("phoneNumber").value("01012345678"))
                .andExpect(jsonPath("basicAddress").value("basicAddress"))
                .andExpect(jsonPath("detailAddress").value("detailAddress"))
                .andExpect(jsonPath("latitude").value(0.0))
                .andExpect(jsonPath("longitude").value(0.0));
    }

    @Test
    @WithFakeUser
    @DisplayName("개인정보를 수정할 수 있다.")
    void changePrivateInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/members/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(MemberFixture.fixtureMemberPrivateInformationUpdate("accountName","12345678", "01012345678")))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true));
    }


}
