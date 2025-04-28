package com.yzgeneration.evc.domain.member.controller;

import com.yzgeneration.evc.common.dto.CommonResponse;
import com.yzgeneration.evc.domain.member.dto.ChangeEmail;
import com.yzgeneration.evc.domain.member.dto.ChangePassword;
import com.yzgeneration.evc.domain.member.dto.MemberPrivateInfoResponse;
import com.yzgeneration.evc.domain.member.dto.MemberPrivateInfoUpdate;
import com.yzgeneration.evc.domain.member.service.MemberAccountService;
import com.yzgeneration.evc.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members/account")
@RequiredArgsConstructor
public class MemberAccountController {

    // TODO 계정찾기를 하고 싶으면 전화번호로 찾아야됨(전화번호 등록 ui 필요) -> 전화번호(문자) 인증 api 필요 (네이버 sms api)
    private final MemberAccountService memberAccountService;

    @PostMapping("/email")
    public CommonResponse changeEmail(@RequestBody ChangeEmail changeEmail, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        memberAccountService.changeEmail(changeEmail.getEmail(), memberPrincipal.getId());
        return CommonResponse.success();
    }

    @PostMapping("/password")
    public CommonResponse changePassword(@RequestBody ChangePassword changePassword, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        memberAccountService.changePassword(changePassword.getOldPassword(), changePassword.getNewPassword(), memberPrincipal.getId());
        return CommonResponse.success();
    }

    @GetMapping
    public MemberPrivateInfoResponse getPrivateInfo(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return memberAccountService.getPrivateInfo(memberPrincipal.getId());
    }

    @PostMapping
    public CommonResponse updatePrivateInfo(@RequestBody MemberPrivateInfoUpdate request, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        memberAccountService.updatePrivateInfo(request.getAccountName(), request.getAccountNumber(), request.getPhoneNumber(), memberPrincipal.getId());
        return CommonResponse.success();
    }



}
