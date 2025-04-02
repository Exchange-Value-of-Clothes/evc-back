package com.yzgeneration.evc.member.service;

import com.yzgeneration.evc.domain.image.implement.ProfileImageUpdater;
import com.yzgeneration.evc.domain.member.dto.ProfileResponse;
import com.yzgeneration.evc.domain.member.dto.UpdateProfileRequest;
import com.yzgeneration.evc.domain.member.dto.UpdateProfileResponse;
import com.yzgeneration.evc.domain.member.implement.MemberUpdater;
import com.yzgeneration.evc.domain.member.service.MemberProfileService;
import com.yzgeneration.evc.domain.point.model.MemberPoint;
import com.yzgeneration.evc.fixture.MemberFixture;
import com.yzgeneration.evc.mock.image.FakeProfileImageRepository;
import com.yzgeneration.evc.mock.member.FakeMemberRepository;
import com.yzgeneration.evc.mock.member.StubMemberProfileRepository;
import com.yzgeneration.evc.mock.point.FakeMemberPointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MemberProfileServiceTest {

    private MemberProfileService memberProfileService;
    private FakeMemberRepository memberRepository;
    private FakeMemberPointRepository memberPointRepository;

    @BeforeEach
    void init() {
        memberRepository = new FakeMemberRepository();
        memberPointRepository = new FakeMemberPointRepository();
        memberProfileService = new MemberProfileService(
                new StubMemberProfileRepository(),
                new MemberUpdater(memberRepository),
                new ProfileImageUpdater(new FakeProfileImageRepository()),
                memberPointRepository
        );
    }

    @Test
    @DisplayName("프로필정보를 조회할 수 있다.")
    void get() {
        // given
        // when
        ProfileResponse profileResponse = memberProfileService.get(1L);

        // then
        assertThat(profileResponse.getImageName()).isEqualTo("imageName");
    }

    @Test
    @DisplayName("프로필 정보를 업데이트 할 수 있다.")
    void update() {
        // given
        String nickname = "nickname";
        String imageName = "imageName";
        UpdateProfileRequest updateProfileRequest = MemberFixture.fixtureUpdateProfileRequest(nickname, imageName);
        Long memberId = 1L;
        memberRepository.save(MemberFixture.withFakeUser());
        memberPointRepository.save(MemberPoint.create(memberId, 1000));
        // when
        UpdateProfileResponse response = memberProfileService.update(updateProfileRequest, memberId);
        assertThat(response.getImageName()).isEqualTo(imageName);
        assertThat(response.getNickname()).startsWith(nickname);
    }
}
