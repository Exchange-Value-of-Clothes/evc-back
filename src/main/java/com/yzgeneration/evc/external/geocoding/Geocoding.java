package com.yzgeneration.evc.external.geocoding;


import com.yzgeneration.evc.domain.delivery.dto.SearchCoordinateResponse;

public interface Geocoding {
    SearchCoordinateResponse search(String query);
}
