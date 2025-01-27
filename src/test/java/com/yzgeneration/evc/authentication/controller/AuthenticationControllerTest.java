package com.yzgeneration.evc.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.authentication.dto.AuthenticationToken;
import com.yzgeneration.evc.authentication.service.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
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
                        .content(objectMapper.writeValueAsString(AuthenticationToken.create("accessToken", "refreshToken")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.authenticationToken.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.authenticationToken.refreshToken").value("refreshToken"));

    }
}