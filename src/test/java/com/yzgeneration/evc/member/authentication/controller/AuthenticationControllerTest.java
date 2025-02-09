package com.yzgeneration.evc.member.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.domain.member.authentication.controller.AuthenticationController;
import com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationToken;
import com.yzgeneration.evc.domain.member.authentication.service.AuthenticationService;
import com.yzgeneration.evc.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = AuthenticationController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
        })
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("로그인한다.")
    @WithMockUser
    void login() throws Exception {
        given(authenticationService.login(any(), any()))
                .willReturn(AuthenticationToken.create("accessToken", "refreshToken"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .content(objectMapper.writeValueAsString(MemberFixture.fixLoginRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.authenticationToken.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.authenticationToken.refreshToken").value("refreshToken"));

    }

    @Test
    @DisplayName("토큰 재발급한다.")
    @WithMockUser
    void refresh() throws Exception {
        given(authenticationService.refresh(any()))
                .willReturn(AuthenticationToken.create("accessToken", "refreshToken"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/refresh")
                        .content(objectMapper.writeValueAsString(MemberFixture.fixRefreshRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.authenticationToken.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.authenticationToken.refreshToken").value("refreshToken"));

    }

    @Test
    @DisplayName("소셜 로그인한다.")
    @WithMockUser
    void socialLogin() throws Exception {
        given(authenticationService.authorizationCode(any(), any()))
                .willReturn(ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "url").build());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/social")
                        .queryParam("provider_type", "GOOGLE")
                        .queryParam("state", "1234")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isFound());

    }
}