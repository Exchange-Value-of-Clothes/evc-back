package com.yzgeneration.evc.external.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.common.CustomUtil;
import com.yzgeneration.evc.domain.delivery.dto.DeliveryCreate;
import com.yzgeneration.evc.exception.ExternalApiException;
import com.yzgeneration.evc.exception.ExternalApiExceptionV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoMobility implements Mobility {

    @Value("${KAKAO_MOBILITY_KEY}")
    private String apiKey;

    @Value("${KAKAO_VENDOR_ID}")
    private String vendorId;

    private final ObjectMapper objectMapper;

    public String authCheck() {
        String authorization = getAuthorization();
        RestClient restClient = RestClient.create("https://open-api-logistics.kakaomobility.com/v1/auth/check");
        restClient.get()
                .header("Authorization", authorization)
                .header("Content-Type", "application/json")
                .header("vendor", vendorId)
                .retrieve().toBodilessEntity();
        return authorization;
    }

    @Override
    public KakaoMobilityOrderResponse delivery(DeliveryCreate deliveryCreate, String orderId) {
        KakaoMobilityOrder kakaoMobilityOrder = KakaoMobilityOrder.from(deliveryCreate,orderId);

        String authorization = getAuthorization();
        RestClient restClient = RestClient.builder()
                .baseUrl("https://open-api-logistics.kakaomobility.com/goa-sandbox-service/api/v2/orders")
                .requestInterceptor((request, body, execution) -> {
                    logRequest(request, body);
                    return execution.execute(request, body);
                })
                .build();
        return restClient.post()
                .header("accept", "application/json")
                .header("Authorization", authorization)
                .header("Content-Type", "application/json")
                .header("vendor", vendorId)
                .header("User-Agent", "curl/7.87.0")
                .body(CustomUtil.jsonSerialization(kakaoMobilityOrder))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    // 에러 응답 바디를 String으로 읽어와서 ExternalApiException으로 throw
                    String body = new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    throw new ExternalApiExceptionV2(res.getStatusCode().value(), body);
                })
                .toEntity(KakaoMobilityOrderResponse.class)
                .getBody();
    }

    @Override
    public GetKakaoMobilityOrder get(String orderId) {
        String authorization = getAuthorization();
        RestClient restClient = RestClient.create("https://open-api-logistics.kakaomobility.com/goa-sandbox-service/api/v2/orders/" + orderId);
        return restClient.get()
                .header("Authorization", authorization)
                .header("Content-Type", "application/json")
                .header("vendor", vendorId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    try (InputStream bodyStream = res.getBody()) {
                        String body = new String(bodyStream.readAllBytes(), StandardCharsets.UTF_8);
                        log.error("카카오 응답 바디: {}", body);
                        KakaoMobilityErrorResponse errorResponse = objectMapper.readValue(body, KakaoMobilityErrorResponse.class);
                        throw new ExternalApiException("카카오 모빌리티 api, 배송 주문 조회 시 오류", errorResponse.getMessage(), errorResponse.getCode());
                    } catch (IOException e) {
                        throw new RuntimeException("카카오 모빌리티 api, 응답 본문 읽기 실패");
                    }
                })
                .toEntity(GetKakaoMobilityOrder.class)
                .getBody();
    }


    private String getAuthorization() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = UUID.randomUUID().toString();
        String sign = generateSignature(timestamp, nonce, apiKey);
        return generateAuthorization(timestamp, nonce, sign);
    }

    private String generateAuthorization(String timestamp, String nonce, String sign) {
        String authValue = timestamp + "$$" + nonce + "$$" + sign;
        return Base64.getEncoder().encodeToString(authValue.getBytes());
    }

    private String generateSignature(String timestamp, String nonce, String apiKey) {
        String plainText = timestamp + nonce + apiKey;
        return signatureSHA512(plainText);
    }

    private String signatureSHA512(String plainText) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(plainText.getBytes());
        return String.format("%0128x", new BigInteger(1, md.digest()));
    }

    private void logRequest(HttpRequest request, byte[] body) {
        log.info("RequestHeader info : {}", request.getHeaders());
        log.debug("RequestHeader debug: {}", request.getHeaders());

    }

}
