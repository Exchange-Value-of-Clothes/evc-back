package com.yzgeneration.evc.docs.authentication;

import com.yzgeneration.evc.domain.member.authentication.controller.AuthenticationController;
import com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationRequest;
import com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationToken;
import com.yzgeneration.evc.domain.member.authentication.service.AuthenticationService;
import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class AuthenticationControllerDocsTest extends RestDocsSupport {

    private final AuthenticationService authenticationService = Mockito.mock(AuthenticationService.class);

    @Override
    protected Object initController() {
        return new AuthenticationController(authenticationService);
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        AuthenticationRequest.LoginRequest loginRequest = MemberFixture.fixLoginRequest();
        given(authenticationService.login(any(), any()))
                .willReturn(AuthenticationToken.create("accessToken", "refreshToken"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticationToken.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.authenticationToken.refreshToken").value("refreshToken"))
                .andDo(document("authentication-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("authenticationToken.accessToken").type(JsonFieldType.STRING)
                                        .description("액세스 토큰"),
                                fieldWithPath("authenticationToken.refreshToken").type(JsonFieldType.STRING)
                                        .description("리프레시 토큰")
                        )
                        ));
    }

    @Test
    @DisplayName("토큰 재발급")
    void refresh() throws Exception {
        given(authenticationService.refresh(any()))
                .willReturn(AuthenticationToken.create("accessToken", "refreshToken"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/refresh")
                .content(objectMapper.writeValueAsString(MemberFixture.fixRefreshRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticationToken.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.authenticationToken.refreshToken").value("refreshToken"))
                .andDo(document("authentication-refresh",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                        .description("리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("authenticationToken.accessToken").type(JsonFieldType.STRING)
                                        .description("액세스 토큰"),
                                fieldWithPath("authenticationToken.refreshToken").type(JsonFieldType.STRING)
                                        .description("리프레시 토큰")
                        )
                        ));

    }

    @Test
    @DisplayName("소셜 로그인한다.")
    @WithMockUser
    void socialLogin() throws Exception {
        given(authenticationService.authorizationCode(any(), any()))
                .willReturn(ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "url").build());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/social")
                        .queryParam("provider_type", "GOOGLE")
                        .queryParam("state", "1234"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andDo(document("authentication-socialLogin",
                        queryParameters(
                                parameterWithName("provider_type").description("소셜 로그인 플랫폼(대문자) e.g. GOOGLE,KAKAO,NAVER"),
                                parameterWithName("state").description("csrf 토큰 (랜덤한 문자열)")
                        )));

    }


}
