package com.yzgeneration.evc.docs.authentication;

import com.yzgeneration.evc.domain.member.authentication.controller.AuthenticationController;
import com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationRequest;
import com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationResponse;
import com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationToken;
import com.yzgeneration.evc.domain.member.authentication.service.AuthenticationService;
import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.fixture.MemberFixture;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.*;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
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
        AuthenticationToken authenticationToken = AuthenticationToken.create("accessToken", "refreshToken");
        ResponseCookie cookie = ResponseCookie.from("refresh_token", authenticationToken.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        given(authenticationService.login(any(), any()))
                .willReturn(
                        ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(new AuthenticationResponse.LoginResponse(authenticationToken.getAccessToken()))
                );
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(MockMvcResultMatchers.cookie().exists("refresh_token"))
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
                                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                        .description("액세스 토큰")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.SET_COOKIE).description("Refresh 토큰이 담긴 HTTP Only 쿠키")
                        )

                ));
    }

    @Test
    @DisplayName("토큰 재발급")
    void refresh() throws Exception {
        AuthenticationToken authenticationToken = AuthenticationToken.create("accessToken", "refreshToken");
        ResponseCookie cookie = ResponseCookie.from("refresh_token", authenticationToken.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        Cookie refreshToken = new Cookie("refresh_token", authenticationToken.getRefreshToken());
        given(authenticationService.refresh(any()))
                .willReturn(
                        ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(new AuthenticationResponse.RefreshResponse(authenticationToken.getAccessToken()))
                );
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/refresh")
                        .content(objectMapper.writeValueAsString(MemberFixture.fixRefreshRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(refreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(MockMvcResultMatchers.cookie().exists("refresh_token"))
                .andDo(document("authentication-refresh",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                        .description("액세스 토큰")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.SET_COOKIE).description("Refresh 토큰이 담긴 HTTP Only 쿠키")
                        )
                ));

    }

    @Test
    @DisplayName("소셜 로그인한다.")
    @WithMockUser
    void socialLogin() throws Exception {
        given(authenticationService.authorizationCode(any(), any()))
                .willReturn(ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "http://localhost:3000/social-login-success").build());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/social")
                        .queryParam("provider_type", "GOOGLE")
                        .queryParam("state", "1234"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost:3000/social-login-success"))
                .andDo(document("authentication-socialLogin",
                        queryParameters(
                                parameterWithName("provider_type").description("소셜 로그인 플랫폼(대문자) e.g. GOOGLE,KAKAO,NAVER"),
                                parameterWithName("state").description("csrf 토큰 (랜덤한 문자열)")
                        )));

    }


}
