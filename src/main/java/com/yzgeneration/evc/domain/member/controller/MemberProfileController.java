package com.yzgeneration.evc.domain.member.controller;

import com.yzgeneration.evc.domain.member.dto.ProfileResponse;
import com.yzgeneration.evc.domain.member.service.MemberProfileService;
import com.yzgeneration.evc.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members/profile")
@RequiredArgsConstructor
public class MemberProfileController { // TODO 프로필과 게정 구분

    private final MemberProfileService memberProfileService;

    @GetMapping("/me")
    public ProfileResponse getMyInformation(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return memberProfileService.get(memberPrincipal.getId());
    }
}
