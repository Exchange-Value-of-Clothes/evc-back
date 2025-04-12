package com.yzgeneration.evc.domain.delivery.dto;

import com.yzgeneration.evc.external.geocoding.KakaoSearchCoordinateResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class SearchCoordinateResponse {
    private List<Datum> data;

    @Getter
    @NoArgsConstructor
    private static class Datum {
        private String address_name;
        private String x;
        private String y;

        private Datum(String address_name, String x, String y) {
            this.address_name = address_name;
            this.x = x;
            this.y = y;
        }
    }

    public SearchCoordinateResponse(KakaoSearchCoordinateResponse response) {
        this.data = new ArrayList<>();
        if (response != null && response.getDocuments() != null) {
            for (KakaoSearchCoordinateResponse.Document document : response.getDocuments()) {
                data.add(new Datum(document.getAddress_name(), document.getX(), document.getY()));
            }
        }
    }

}
