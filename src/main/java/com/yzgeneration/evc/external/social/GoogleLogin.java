package com.yzgeneration.evc.external.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleLogin implements SocialLogin {

    @Value("${GOOGLE_CLIENT_ID}")
    private String GOOGLE_CLIENT_ID;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${GOOGLE_REDIRECT_URI}")
    private String GOOGLE_REDIRECT_URI;

    private final CsrfRepository csrfRepository;
    private final ObjectMapper objectMapper;

    @Getter
    @NoArgsConstructor
    private static class GoogleTokenResponse {
        private String access_token;
        private String id_token;
        private String scope;
    }

    @Override
    public ResponseEntity<Void> getAuthorizationCode(String state) {
        csrfRepository.save(state, "GOOGLE");
        String url = UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("response_type", "code")
                .queryParam("client_id", GOOGLE_CLIENT_ID)
                .queryParam("redirect_uri", GOOGLE_REDIRECT_URI)
                .queryParam("scope", "openid%20email%20profile")
                .queryParam("state", state)
                .build()
                .toUriString();
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, url).build();
    }

    @Override
    public String getToken(String authorizeCode, String state) {
        csrfRepository.valid(state, "GOOGLE");
        RestClient restClient = RestClient.create("https://oauth2.googleapis.com/token");
        GoogleTokenResponse response = restClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(generateRequestBody(authorizeCode))
                .retrieve()
                .toEntity(GoogleTokenResponse.class)
                .getBody();

        if (response == null || response.id_token == null) {
            throw new CustomException(ErrorCode.SOCIAL_PLATFORM_SERVER_ERROR, "소셜 로그인 중 id_token 요청에 실패했습니다.");
        }
        return response.id_token;
    }

    @Override
    public SocialPlatform getSocialPlatform() {
        return SocialPlatform.GOOGLE;
    }

    @Override
    public SocialUserProfile getUserProfile(String idToken) {
        try {
            String[] payloadAndSignature = idToken.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(payloadAndSignature[1]));
            JsonNode jsonNode = objectMapper.readTree(payload);
            String sub = jsonNode.get("sub").asText();
            String email = jsonNode.has("email") ? jsonNode.get("email").asText() : null;
            String name = jsonNode.has("name") ? jsonNode.get("name").asText() : null;
            String picture = jsonNode.has("picture") ? jsonNode.get("picture").asText() : null;
            return new GoogleUserProfile(sub, email, picture, name);
        } catch (JsonProcessingException | IllegalArgumentException e) {
            log.error("id_token 파싱 실패 {}",e.getMessage());
            throw new CustomException(ErrorCode.TOKEN_UNAUTHORIZED, "소셜 로그인 중 id_token 파싱 오류");
        }

    }

    private MultiValueMap<String, String> generateRequestBody(String authorizeCode) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", GOOGLE_CLIENT_ID);
        requestBody.add("client_secret", GOOGLE_CLIENT_SECRET);
        requestBody.add("redirect_uri", GOOGLE_REDIRECT_URI);
        requestBody.add("code", authorizeCode);
        return requestBody;
    }
}
