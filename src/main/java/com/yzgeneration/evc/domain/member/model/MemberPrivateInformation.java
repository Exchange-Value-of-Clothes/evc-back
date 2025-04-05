package com.yzgeneration.evc.domain.member.model;

import com.yzgeneration.evc.common.implement.port.RandomHolder;
import com.yzgeneration.evc.external.social.SocialUserProfile;
import lombok.Builder;
import lombok.Getter;

import java.util.Random;


@Getter
@Builder
public class MemberPrivateInformation {

    private String nickname;
    private String email;
    private String accountNumber;
    private String accountName;
    private String phoneNumber;

    private static final Random RANDOM = new Random();

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
                .build();
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname + "#"+ String.format("%04d", RANDOM.nextInt(10000));
    }

    public String getRawNickname() {
        int i = nickname.indexOf("#");
        return nickname.substring(0,i);
    }

    public void changeEmail(String email) {
        this.email = email;
    }

}
