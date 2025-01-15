package com.yzgeneration.evc.fixture;

import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.yzgeneration.evc.member.enums.MemberRole;
import com.yzgeneration.evc.member.enums.MemberStatus;
import com.yzgeneration.evc.member.enums.ProviderType;
import com.yzgeneration.evc.member.model.Member;
import com.yzgeneration.evc.member.model.MemberAuthenticationInformation;
import com.yzgeneration.evc.member.model.MemberPrivateInformation;

import java.util.List;

import static com.yzgeneration.evc.member.dto.MemberRequest.*;

public abstract class MemberFixture {

    public static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(new FailoverIntrospector(
                    List.of(BuilderArbitraryIntrospector.INSTANCE,FieldReflectionArbitraryIntrospector.INSTANCE)
            ))
            .defaultNotNull(true)
            .build();

    public static EmailSignup fixEmailSignup() {
        return fixtureMonkey.giveMeBuilder(EmailSignup.class)
                .set("email", "ssar@naver.com")
                .set("nickname", "구코딩")
                .set("password", "1234")
                .set("checkPassword", "1234")
                .sample();
    }

    private static ArbitraryBuilder<MemberAuthenticationInformation> fixPassword() {
        return fixtureMonkey.giveMeBuilder(MemberAuthenticationInformation.class)
                .set("password", "1234");
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

    public static Member createdMyEmail() {
        return Member.builder()
                .memberAuthenticationInformation(emailMemberAuthenticationInfo())
                .memberPrivateInformation(fixNicknameAndEmail())
                .memberStatus(MemberStatus.PENDING)
                .memberRole(MemberRole.USER)
                .build();
    }

}
