package com.yzgeneration.evc.domain.delivery.service;

import com.yzgeneration.evc.domain.delivery.dto.SearchCoordinateResponse;
import com.yzgeneration.evc.domain.delivery.infrastructure.AddressRepository;
import com.yzgeneration.evc.domain.delivery.model.Address;
import com.yzgeneration.evc.external.geocoding.Geocoding;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AddressService {

    private final Geocoding geocoding;
    private final AddressRepository addressRepository;

    public SearchCoordinateResponse searchCoordinate(String addressName) {
        return geocoding.search(addressName);
    }

    public void create(String basicAddress, String detailAddress, double latitude, double longitude, Long memberId) {
        addressRepository.findByMemberId(memberId)
                .ifPresentOrElse(
                        address -> {
                            address.update(basicAddress, detailAddress, latitude, longitude);
                            addressRepository.save(address);
                        }
                        ,
                        () -> addressRepository.save(Address.create(memberId, basicAddress, detailAddress, latitude, longitude))
                );
    }

}
