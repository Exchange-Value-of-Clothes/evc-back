package com.yzgeneration.evc.domain.member.controller;

import com.yzgeneration.evc.domain.member.dto.MyInformationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members/profile")
@RequiredArgsConstructor
public class MemberProfileController { // TODO 프로필과 게정 구분

    @GetMapping("/me")
    public MyInformationResponse getMyInformation() {
        return null;
    }
}
