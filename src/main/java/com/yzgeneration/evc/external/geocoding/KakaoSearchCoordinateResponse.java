package com.yzgeneration.evc.external.geocoding;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoSearchCoordinateResponse {

    private List<Document> documents;

    @Getter
    @NoArgsConstructor
    public static class Document{
        private String address_name;
        private String address_type; // REGION(지명), ROAD(도로명), REGION_ADDR(지번 주소), ROAD_ADDR(도로명 주소)
        private String x;
        private String y;
    }
}
