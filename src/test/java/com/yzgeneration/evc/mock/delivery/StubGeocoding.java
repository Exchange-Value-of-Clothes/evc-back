package com.yzgeneration.evc.mock.delivery;

import com.yzgeneration.evc.domain.delivery.dto.SearchCoordinateResponse;
import com.yzgeneration.evc.external.geocoding.Geocoding;
import com.yzgeneration.evc.fixture.DeliveryFixture;

public class StubGeocoding implements Geocoding {

    @Override
    public SearchCoordinateResponse search(String query) {
        return DeliveryFixture.createSearchCoordinateResponse();
    }
}
