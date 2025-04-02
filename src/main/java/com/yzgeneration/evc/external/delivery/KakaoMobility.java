package com.yzgeneration.evc.external.delivery;

import com.yzgeneration.evc.common.CustomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KakaoMobility {

    @Value("${KAKAO_MOBILITY_KEY}")
    private String apiKey;

    @Value("${KAKAO_VENDOR_ID}")
    private String vendorId;

    public void authCheck() throws NoSuchAlgorithmException {
        String authorization = getAuthorization();
        RestClient restClient = RestClient.create("https://open-api-logistics.kakaomobility.com/v1/auth/check");
        restClient.get()
                .header("Authorization", authorization)
                .header("Content-Type", "application/json")
                .header("vendor", vendorId)
                .retrieve().toBodilessEntity();
    }

    public void createQuickOrder(KakaoMobilityOrder kakaoMobilityOrder) throws NoSuchAlgorithmException {
        String authorization = getAuthorization();
        RestClient restClient = RestClient.create("https://open-api-logistics.kakaomobility.com/goa-sandbox-service/api/v2/orders");
        restClient.post()
                .header("Authorization", authorization)
                .header("Content-Type", "application/json")
                .header("vendor", vendorId)
                .body(CustomUtil.jsonSerialization(kakaoMobilityOrder));
    }

    private String generateSignature(String timestamp, String nonce, String apiKey)
            throws NoSuchAlgorithmException {
        String plainText = timestamp + nonce + apiKey;
        return signatureSHA512(plainText);
    }

    private String signatureSHA512(String plainText)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(plainText.getBytes());
        return String.format("%0128x", new BigInteger(1, md.digest()));
    }

    private String getAuthorization(String timestamp, String nonce, String sign) {
        String authValue = timestamp + "$$" + nonce + "$$" + sign;
        return Base64.getEncoder().encodeToString(authValue.getBytes());
    }

    private String getAuthorization() throws NoSuchAlgorithmException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = UUID.randomUUID().toString();
        String sign = generateSignature(timestamp, nonce, apiKey);
        return getAuthorization(timestamp, nonce, sign);
    }

}
