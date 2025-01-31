package com.yzgeneration.evc.external.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.common.exception.CustomException;
import com.yzgeneration.evc.common.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
@RequiredArgsConstructor
public class NaverLogin implements SocialLogin {


    @Value("${NAVER_CLIENT_ID}")
    private String NAVER_CLIENT_ID;

    @Value("${NAVER_CLIENT_SECRET}")
    private String NAVER_CLIENT_SECRET;

    @Value("${NAVER_REDIRECT_URI}")
    private String NAVER_REDIRECT_URI;

    private final ObjectMapper objectMapper;

    @Getter
    @NoArgsConstructor
    private static class NaverLoginAuthenticationResponse {
        private String code;
        private String state;
    }

    @Getter
    @NoArgsConstructor
    private class NaverAccessTokenRequest {
        private String grant_type = "authorization_code";
        private String client_id = NAVER_CLIENT_ID;
        private String client_secret = NAVER_CLIENT_SECRET;
        private String code;

        public NaverAccessTokenRequest(String code) {
            this.code = code;
        }
    }

    @Getter
    @NoArgsConstructor
    private static class NaverAccessTokenResponse {
        private String access_token;
    }

    @Override
    public String getAuthorizationCode(String state) {
        RestClient restClient = RestClient.create("https://nid.naver.com/oauth2.0/authorize");
        NaverLoginAuthenticationResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("response_type", "code")
                        .queryParam("client_id", NAVER_CLIENT_ID)
                        .queryParam("redirect_uri", NAVER_REDIRECT_URI)
                        .queryParam("state", state)
                        .build())
                .retrieve()
                .toEntity(NaverLoginAuthenticationResponse.class).getBody();
        if(response == null || response.code == null) {
            throw new CustomException(ErrorCode.SOCIAL_PLATFORM_SERVER_ERROR, "소셜 로그인 중 인가코드 요청에 실패했습니다.");
        }
        return response.code;
    }

    @Override
    public String getAccessToken(String authorizeCode, String state) throws JsonProcessingException {
        RestClient restClient = RestClient.create("https://nid.naver.com/oauth2.0/token");
        NaverAccessTokenResponse response = restClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(new NaverAccessTokenRequest(authorizeCode)))
                .retrieve()
                .toEntity(NaverAccessTokenResponse.class).getBody();
        if(response == null || response.access_token == null) {
            throw new CustomException(ErrorCode.SOCIAL_PLATFORM_SERVER_ERROR, "소셜 로그인 중 액세스 토큰 요청에 실패했습니다.");
        }
        return response.access_token;
    }

    @Override
    public SocialPlatform getSocialPlatform() {
        return SocialPlatform.NAVER;
    }
}
