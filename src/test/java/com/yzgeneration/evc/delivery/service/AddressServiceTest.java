package com.yzgeneration.evc.delivery.service;

import com.yzgeneration.evc.domain.delivery.dto.SearchCoordinateResponse;
import com.yzgeneration.evc.domain.delivery.service.AddressService;
import com.yzgeneration.evc.mock.delivery.FakeAddressRepository;
import com.yzgeneration.evc.mock.delivery.StubGeocoding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AddressServiceTest {

    private AddressService addressService;
    private FakeAddressRepository fakeAddressRepository;


    @BeforeEach
    void setUp() {
        fakeAddressRepository = new FakeAddressRepository();
        addressService = new AddressService(new StubGeocoding(), fakeAddressRepository);
    }

    @Test
    @DisplayName("실 주소 및 위도 경도를 얻을 수 있다.")
    void search() {
        // given
        // when
        SearchCoordinateResponse response = addressService.searchCoordinate("a");

        // then
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("주소를 저장할 수 있고 이미 해당 회원의 주소가 있는경우 업데이트한다.")
    void create() {
        // given
        addressService.create("basicAddress", "detailAddress", 0.0, 0.0, 1L);
        // when
        addressService.create("basicAddress", "detailAddress", 0.0, 0.0, 1L);

        // then
        assertThat(fakeAddressRepository.getSize()).isEqualTo(1);
    }
}
