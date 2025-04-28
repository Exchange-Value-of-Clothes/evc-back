package com.yzgeneration.evc.domain.delivery.infrastructure;

import com.yzgeneration.evc.domain.delivery.model.Address;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AddressRepositoryImpl implements AddressRepository {

    private final AddressJpaRepository jpaRepository;

    @Override
    public Address save(Address address) {
        return jpaRepository.save(AddressEntity.from(address)).toModel();
    }

    @Override
    public Optional<Address> findByMemberId(Long memberId) {
        return jpaRepository.findByMemberId(memberId).map(AddressEntity::toModel);
    }

    @Override
    public Address getByMemberId(Long memberId) {
        return findByMemberId(memberId).orElseThrow(()-> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));
    }
}
