package com.yzgeneration.evc.external.geocoding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.domain.delivery.dto.SearchCoordinateResponse;
import com.yzgeneration.evc.exception.ExternalApiException;
import com.yzgeneration.evc.external.KakaoKapiErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoLocal implements Geocoding {

    @Value("${KAKAO_CLIENT_ID}")
    private String KAKAO_CLIENT_ID;

    private final ObjectMapper objectMapper;

    public SearchCoordinateResponse search(String query) {
        String url = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com/v2/local/search/address.json")
                .queryParam("query", query)
                .build()
                .toUriString();
        RestClient restClient = RestClient.create();
        KakaoSearchCoordinateResponse response = restClient.get()
                .uri(url)
                .header("Authorization", "KakaoAK " + KAKAO_CLIENT_ID)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    KakaoKapiErrorResponse errorResponse = objectMapper.readValue(res.getBody(), KakaoKapiErrorResponse.class);
                    throw new ExternalApiException("카카오 로컬 api, 주소를 좌표로 변환 중 에러발생", errorResponse.getMsg(), errorResponse.getCode());
                })
                .toEntity(KakaoSearchCoordinateResponse.class)
                .getBody();
        return new SearchCoordinateResponse(response);
    }

}
