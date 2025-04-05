package com.yzgeneration.evc.member.service;

import com.yzgeneration.evc.domain.member.service.MemberAccountService;
import com.yzgeneration.evc.fixture.MemberFixture;
import com.yzgeneration.evc.mock.member.FakeMemberRepository;
import com.yzgeneration.evc.mock.member.SpyPasswordProcessor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MemberAccountServiceTest {

    private MemberAccountService memberAccountService;
    private FakeMemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberRepository();
        memberAccountService = new MemberAccountService(
                memberRepository,
                new SpyPasswordProcessor()
        );
    }

    @Test
    @DisplayName("이메일을 변경할 수 있다.")
    void changeEmail() {
        // given
        memberRepository.save(MemberFixture.createdByEmail_ACTIVE());

        // when
        memberAccountService.changeEmail("love@naver.com", 1L);

        // then
        assertThat(memberRepository.getById(1L).getMemberPrivateInformation().getEmail()).isEqualTo("love@naver.com");
    }

    @Test
    @DisplayName("비밀번호를 변경할 수 있다.")
    void changePassword() {
        // given
        memberRepository.save(MemberFixture.createdByEmail_ACTIVE());

        // when
        memberAccountService.changePassword("12345678","00000000", 1L);

        // then
        assertThat(memberRepository.getById(1L).getMemberAuthenticationInformation().getPassword()).isEqualTo("00000000");
    }


}
