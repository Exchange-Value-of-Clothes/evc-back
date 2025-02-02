package com.yzgeneration.evc.domain.member.model;

import com.yzgeneration.evc.common.implement.port.RandomHolder;
import com.yzgeneration.evc.external.social.SocialUserProfile;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class MemberPrivateInformation {

    private String nickname;
    private String email;
    private String accountNumber;
    private String accountName;
    private String phoneNumber;
    private String profileImage;
    private int point;

    public static MemberPrivateInformation createdByEmail(String nickname, String email, RandomHolder randomHolder) {
        return MemberPrivateInformation.builder()
                .nickname(nickname + "#" + randomHolder.randomFourDigit())
                .email(email)
                .build();
    }

    public static MemberPrivateInformation createdBySocialLogin(SocialUserProfile socialUserProfile) {
        return MemberPrivateInformation.builder()
                .nickname(socialUserProfile.getNickname())
                .email(socialUserProfile.getEmail())
                .phoneNumber(socialUserProfile.getPhoneNumber())
                .profileImage(socialUserProfile.getProfileImage())
                .build();
    }

}
