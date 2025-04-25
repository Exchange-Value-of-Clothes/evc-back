package com.yzgeneration.evc.fixture;

import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.yzgeneration.evc.authentication.dto.AuthenticationRequest;
import com.yzgeneration.evc.domain.member.dto.*;
import com.yzgeneration.evc.domain.member.enums.MemberRole;
import com.yzgeneration.evc.domain.member.enums.MemberStatus;
import com.yzgeneration.evc.domain.member.enums.ProviderType;
import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.member.model.MemberAuthenticationInformation;
import com.yzgeneration.evc.domain.member.model.MemberPrivateInformation;


import static com.yzgeneration.evc.domain.member.dto.MemberRequest.*;

public final class MemberFixture extends Fixture{

    private MemberFixture() {}

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

    public static AuthenticationRequest.LoginRequest fixLoginRequest() {
        return fixtureMonkey.giveMeBuilder(AuthenticationRequest.LoginRequest.class)
                .set("email", "ssar@naver.com")
                .set("password", "12345678")
                .sample();
    }

    public static RefreshRequest fixRefreshRequest() {
        return fixtureMonkey.giveMeBuilder(RefreshRequest.class)
                .set("refreshToken", "refreshToken")
                .sample();
    }

    public static Member withFakeUser() {
        return Member.builder()
                .id(1L)
                .memberAuthenticationInformation(emailMemberAuthenticationInfo())
                .memberPrivateInformation(fixNicknameAndEmail())
                .memberStatus(MemberStatus.ACTIVE)
                .memberRole(MemberRole.USER)
                .build();
    }

    public static UpdateProfileRequest fixtureUpdateProfileRequest(String nickname, String imageName) {
        return fixtureMonkey.giveMeBuilder(UpdateProfileRequest.class)
                .set("nickname", nickname)
                .set("imageName", imageName)
                .sample();
    }

    public static ChangeEmail fixtureChangeEmail() {
        return fixtureMonkey.giveMeBuilder(ChangeEmail.class)
                .set("email", "ssar@naver.com")
                .sample();
    }

    public static ChangePassword fixtureChangePassword() {
        return fixtureMonkey.giveMeBuilder(ChangePassword.class)
                .set("oldPassword", "12345678")
                .set("newPassword", "00000000")
                .set("checkPassword", "00000000")
                .sample();
    }

    public static MemberPrivateInfoResponse fixtureMemberPrivateInformation(String accountName, String accountNumber,
                                                                           String phoneNumber) {
        return fixtureMonkey.giveMeBuilder(MemberPrivateInfoResponse.class)
                .set("accountName", accountName)
                .set("accountNumber", accountNumber)
                .set("phoneNumber", phoneNumber)
                .set("basicAddress", "basicAddress")
                .set("detailAddress", "detailAddress")
                .set("latitude", 0.0)
                .set("longitude", 0.0)
                .sample();
    }

    public static MemberPrivateInfoUpdate fixtureMemberPrivateInformationUpdate(String accountName, String accountNumber, String phoneNumber) {
        return fixtureMonkey.giveMeBuilder(MemberPrivateInfoUpdate.class)
                .set("accountName", accountName)
                .set("accountNumber", accountNumber)
                .set("phoneNumber", phoneNumber)
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
