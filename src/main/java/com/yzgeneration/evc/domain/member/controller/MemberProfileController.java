package com.yzgeneration.evc.domain.member.controller;

import com.yzgeneration.evc.common.dto.CommonResponse;
import com.yzgeneration.evc.domain.member.dto.ProfileResponse;
import com.yzgeneration.evc.domain.member.dto.UpdateProfileRequest;
import com.yzgeneration.evc.domain.member.service.MemberProfileService;
import com.yzgeneration.evc.security.MemberPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members/profile")
@RequiredArgsConstructor
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    @PatchMapping
    public CommonResponse update(@RequestBody @Valid UpdateProfileRequest updateProfileRequest, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return CommonResponse.success();
    }

    @GetMapping("/me")
    public ProfileResponse getMyProfile(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return memberProfileService.get(memberPrincipal.getId());
    }
}
