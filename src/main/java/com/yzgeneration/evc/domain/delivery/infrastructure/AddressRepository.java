package com.yzgeneration.evc.domain.delivery.infrastructure;

import com.yzgeneration.evc.domain.delivery.model.Address;

import java.util.Optional;

public interface AddressRepository {
    Address save(Address address);
    Optional<Address> findByMemberId(Long memberId);
    Address getByMemberId(Long memberId);
}
