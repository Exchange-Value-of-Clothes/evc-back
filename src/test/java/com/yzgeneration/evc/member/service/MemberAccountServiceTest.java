package com.yzgeneration.evc.member.service;

import com.yzgeneration.evc.domain.member.dto.MemberPrivateInfoResponse;
import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.domain.member.service.MemberAccountService;
import com.yzgeneration.evc.fixture.MemberFixture;
import com.yzgeneration.evc.mock.member.FakeMemberRepository;
import com.yzgeneration.evc.mock.member.SpyPasswordProcessor;
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

    @Test
    @DisplayName("개인정보를 조회할 수 있다.")
    void getPrivateInformation() {
        // given
        Member member = memberRepository.save(MemberFixture.createdByEmail_ACTIVE());

        // when
        MemberPrivateInfoResponse privateInfo = memberAccountService.getPrivateInfo(member.getId());

        // then
        assertThat(privateInfo).isNotNull();

    }

    @Test
    @DisplayName("개인정보를 수정할 수 있다.")
    void updatePrivateInformation() {
        // given
        Member member = memberRepository.save(MemberFixture.createdByEmail_ACTIVE());
        String accountName = "a";
        String accountNumber = "12345678";
        String phoneNumber = "123456789";
        // when
        memberAccountService.updatePrivateInfo(accountName, accountNumber, phoneNumber, member.getId());

        // then
        MemberPrivateInfoResponse privateInfo = memberAccountService.getPrivateInfo(member.getId());
        assertThat(privateInfo.getAccountName()).isEqualTo(accountName);
        assertThat(privateInfo.getAccountNumber()).isEqualTo(accountNumber);
        assertThat(privateInfo.getPhoneNumber()).isEqualTo(phoneNumber);

    }




}
