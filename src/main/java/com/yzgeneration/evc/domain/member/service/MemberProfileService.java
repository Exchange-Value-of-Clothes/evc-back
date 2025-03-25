package com.yzgeneration.evc.domain.member.service;

import com.yzgeneration.evc.domain.member.dto.ProfileResponse;
import com.yzgeneration.evc.domain.member.infrastructure.MemberProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;
    public ProfileResponse get(Long id) {
        return null;
    }

}
