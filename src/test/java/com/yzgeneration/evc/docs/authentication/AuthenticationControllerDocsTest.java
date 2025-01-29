package com.yzgeneration.evc.docs.authentication;

import com.yzgeneration.evc.authentication.controller.AuthenticationController;
import com.yzgeneration.evc.authentication.dto.AuthenticationToken;
import com.yzgeneration.evc.authentication.service.AuthenticationService;
import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.fixture.AuthenticationFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.yzgeneration.evc.authentication.dto.AuthenticationRequest.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
        LoginRequest loginRequest = AuthenticationFixture.fixLoginRequest();
        given(authenticationService.login(any(), any()))
                .willReturn(AuthenticationToken.create("accessToken", "refreshToken"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
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
                                        .description("리프레쉬 토큰")
                        )
                        ));
    }
}
