package com.yzgeneration.evc.external.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class KaKaoLogin implements SocialLogin {

    @Value("${KAKAO_CLIENT_ID}")
    private String KAKAO_CLIENT_ID;

    @Value("${KAKAO_CLIENT_SECRET}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${KAKAO_REDIRECT_URI}")
    private String KAKAO_REDIRECT_URI;

    private final CsrfRepository csrfRepository;
    private final ObjectMapper objectMapper;

    @Getter
    @NoArgsConstructor
    private static class KakaoTokenResponse {
        private String access_token;
        private String id_token;
        private String scope;
    }

    @Override
    public ResponseEntity<Void> getAuthorizationCode(String state) {
        csrfRepository.save(state, "KAKAO");
        String url = UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", KAKAO_CLIENT_ID)
                .queryParam("redirect_uri", KAKAO_REDIRECT_URI)
                .queryParam("scope", "openid")
                .queryParam("state", state)
                .build()
                .toUriString();
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, url).build();
    }

    @Override
    public String getToken(String authorizeCode, String state) {
        csrfRepository.valid(state, "KAKAO");
        RestClient restClient = RestClient.create("https://kauth.kakao.com/oauth/token");
        KakaoTokenResponse response = restClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(generateRequestBody(authorizeCode))
                .retrieve()
                .toEntity(KakaoTokenResponse.class)
                .getBody();

        if (response == null || response.id_token == null) {
            throw new CustomException(ErrorCode.SOCIAL_PLATFORM_SERVER_ERROR, "소셜 로그인 중 id_token 요청에 실패했습니다.");
        }
        return response.id_token;
    }

    @Override
    public SocialPlatform getSocialPlatform() {
        return SocialPlatform.KAKAO;
    }

    @Override
    public SocialUserProfile getUserProfile(String idToken) {
        String[] payloadAndSignature = idToken.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(payloadAndSignature[1]));
        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            String sub = jsonNode.get("sub").asText();
            String nickname = jsonNode.has("nickname") ? jsonNode.get("nickname").asText() : null;
            String picture = jsonNode.has("picture") ? jsonNode.get("picture").asText() : null;
            return new KakaoUserProfile(sub, nickname, picture);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private MultiValueMap<String, String> generateRequestBody(String authorizeCode) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", KAKAO_CLIENT_ID);
        requestBody.add("client_secret", KAKAO_CLIENT_SECRET);
        requestBody.add("redirect_uri", KAKAO_REDIRECT_URI);
        requestBody.add("code", authorizeCode);
        return requestBody;
    }
}
