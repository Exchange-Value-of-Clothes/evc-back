package com.yzgeneration.evc.external.pg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import com.yzgeneration.evc.exception.PaymentGatewayException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class TossPayments implements PaymentGateway {

    @Value("${TOSS_SECRET}")
    private String secret;

    private final ObjectMapper objectMapper;

    @Override
    public Payment confirm(String orderId, String paymentKey, int amount) {
        String credentials = secret + ":";
        String secretKey =  Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        RestClient restClient = RestClient.create("https://api.tosspayments.com/v1/payments/confirm");
        return restClient.post()
                .header("Authorization", "Basic "+ secretKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createRequestBody(new TossConfirmRequest(orderId, paymentKey, amount)))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, res) -> {
                    TossErrorV2Response errorResponse = objectMapper.readValue(res.getBody(), TossErrorV2Response.class);
                    throw new PaymentGatewayException(errorResponse.getMessage(), res.getStatusCode().value()); // 에러 객체 확인하려면 응답을 받아야되나?
                })
                .toEntity(Payment.class).getBody();
    }



    private String createRequestBody(TossConfirmRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JsonSerializationException, e.getMessage());
        }
    }
}
