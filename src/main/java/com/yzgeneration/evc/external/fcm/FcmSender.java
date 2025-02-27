package com.yzgeneration.evc.external.fcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FcmSender implements NotificationSender {

    private final ObjectMapper objectMapper;

    @Override
    public void send(String deviceToken, String title, String body) {
        String message = createMessage(deviceToken, title, body);
        String accessToken = getAccessToken();
        RestClient restClient = RestClient.create("https://fcm.googleapis.com/v1/projects/evc-app-cd017/messages:send");
        restClient.post()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(message)
                .retrieve()
                .toEntity(Object.class);

        // 토큰이 유효하지 않으면 db에서 비활성화

        // 앱 실행마다 토큰 업데이트?


    }

    private String getAccessToken() {
        String serviceAccount = "firebase/serviceAccountKey.json";
        try {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new ClassPathResource(serviceAccount).getInputStream())
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform")) ;//https://developers.google.com/identity/protocols/oauth2/scopes?hl=ko
            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createMessage(String token, String title, String body) {
        FcmMessage fcmMessage = FcmMessage.create(token, title, body);
        try {
            return objectMapper.writeValueAsString(fcmMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
