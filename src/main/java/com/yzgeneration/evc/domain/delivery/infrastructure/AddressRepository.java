package com.yzgeneration.evc.domain.delivery.infrastructure;

import com.yzgeneration.evc.domain.delivery.model.Address;

public interface AddressRepository {
    Address save(Address address);
}
