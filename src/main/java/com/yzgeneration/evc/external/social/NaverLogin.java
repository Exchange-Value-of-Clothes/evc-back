package com.yzgeneration.evc.external.social;


import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import com.yzgeneration.evc.exception.SocialLoginException;
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


@Component
@RequiredArgsConstructor
public class NaverLogin implements SocialLogin {


    @Value("${NAVER_CLIENT_ID}")
    private String NAVER_CLIENT_ID;

    @Value("${NAVER_CLIENT_SECRET}")
    private String NAVER_CLIENT_SECRET;

    @Value("${NAVER_REDIRECT_URI}")
    private String NAVER_REDIRECT_URI;

    private final CsrfRepository csrfRepository;

    @Getter
    @NoArgsConstructor
    private static class NaverAccessTokenResponse {
        private String access_token;
    }

    @Override
    public ResponseEntity<Void> getAuthorizationCode(String state) {
        csrfRepository.save(state, "NAVER");
        String url = UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", NAVER_CLIENT_ID)
                .queryParam("redirect_uri", NAVER_REDIRECT_URI)
                .queryParam("state", state)
                .build()
                .toUriString();
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, url).build();
    }

    @Override
    public String getToken(String authorizeCode, String state) {
        csrfRepository.valid(state, "NAVER");
        RestClient restClient = RestClient.create("https://nid.naver.com/oauth2.0/token");
        NaverAccessTokenResponse response = restClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(generateRequestBody(authorizeCode))
                .retrieve()
                .toEntity(NaverAccessTokenResponse.class)
                .getBody();

        if (response == null || response.access_token == null) {
            throw new SocialLoginException();
        }
        return response.access_token;
    }

    @Override
    public SocialPlatform getSocialPlatform() {
        return SocialPlatform.NAVER;
    }

    @Override
    public SocialUserProfile getUserProfile(String accessToken) {
        RestClient restClient = RestClient.create("https://openapi.naver.com/v1/nid/me");
        return restClient.get()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .toEntity(NaverUserProfile.class)
                .getBody();

    }

    private MultiValueMap<String, String> generateRequestBody(String authorizeCode) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", NAVER_CLIENT_ID);
        requestBody.add("client_secret", NAVER_CLIENT_SECRET);
        requestBody.add("code", authorizeCode);
        return requestBody;
    }
}
