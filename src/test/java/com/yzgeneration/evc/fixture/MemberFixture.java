package com.yzgeneration.evc.fixture;

import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.yzgeneration.evc.domain.member.enums.MemberRole;
import com.yzgeneration.evc.domain.member.enums.MemberStatus;
import com.yzgeneration.evc.domain.member.enums.ProviderType;
import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.member.model.MemberAuthenticationInformation;
import com.yzgeneration.evc.domain.member.model.MemberPrivateInformation;


import static com.yzgeneration.evc.domain.member.authentication.dto.AuthenticationRequest.*;
import static com.yzgeneration.evc.domain.member.dto.MemberRequest.*;

public abstract class MemberFixture extends Fixture{

    public static EmailSignup fixEmailSignup() {
        return fixtureMonkey.giveMeBuilder(EmailSignup.class)
                .set("email", "ssar@naver.com")
                .set("nickname", "구코딩")
                .set("password", "12345678")
                .set("checkPassword", "12345678")
                .sample();
    }

    public static Member createdByEmail_PENDING() {
        return Member.builder()
                .memberAuthenticationInformation(emailMemberAuthenticationInfo())
                .memberPrivateInformation(fixNicknameAndEmail())
                .memberStatus(MemberStatus.PENDING)
                .memberRole(MemberRole.USER)
                .build();
    }

    public static Member createdByEmail_ACTIVE() {
        return Member.builder()
                .memberAuthenticationInformation(emailMemberAuthenticationInfo())
                .memberPrivateInformation(fixNicknameAndEmail())
                .memberStatus(MemberStatus.ACTIVE)
                .memberRole(MemberRole.USER)
                .build();
    }

    public static LoginRequest fixLoginRequest() {
        return fixtureMonkey.giveMeBuilder(LoginRequest.class)
                .set("email", "ssar@naver.com")
                .set("password", "12345678")
                .sample();
    }

    private static ArbitraryBuilder<MemberAuthenticationInformation> fixPassword() {
        return fixtureMonkey.giveMeBuilder(MemberAuthenticationInformation.class)
                .set("password", "12345678");
    }

    private static MemberAuthenticationInformation emailMemberAuthenticationInfo() {
        return fixPassword()
                .set("providerType", ProviderType.EMAIL)
                .sample();
    }

    private static MemberPrivateInformation fixNicknameAndEmail() {
        return fixtureMonkey.giveMeBuilder(MemberPrivateInformation.class)
                .set("nickname", "구코딩")
                .set("email","ssar@naver.com")
                .sample();
    }

}
