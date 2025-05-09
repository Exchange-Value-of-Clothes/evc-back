package com.yzgeneration.evc.member.service;

import com.yzgeneration.evc.domain.image.implement.ProfileImageUpdater;
import com.yzgeneration.evc.domain.image.model.ProfileImage;
import com.yzgeneration.evc.domain.member.dto.ProfileResponse;
import com.yzgeneration.evc.domain.member.implement.MemberUpdater;
import com.yzgeneration.evc.domain.member.service.MemberProfileService;
import com.yzgeneration.evc.domain.point.model.MemberPoint;
import com.yzgeneration.evc.fixture.MemberFixture;
import com.yzgeneration.evc.mock.image.StubProfileImageRepository;
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
    private StubProfileImageRepository profileImageRepository;

    @BeforeEach
    void init() {
        memberRepository = new FakeMemberRepository();
        memberPointRepository = new FakeMemberPointRepository();
        profileImageRepository = new StubProfileImageRepository();
        memberProfileService = new MemberProfileService(
                new StubMemberProfileRepository(),
                new MemberUpdater(memberRepository),
                new ProfileImageUpdater(profileImageRepository),
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

    // input imageName= Null
    // case 1 (소셜로그인): ProfileImage(imageName=Null, imageUrl="http...") o
    // case 2 (소셜로그인): ProfileImage(imageName=Null, imageUrl=null) o
    // case 3 (일반로그인): ProfileImage(imageName=Null, imageUrl=null) o
    // case 4 (일반로그인): ProfileImage(imageName="smile", imageUrl=null) x

    // input imageName= "smile"
    // case 1 (소셜로그인): ProfileImage(imageName=Null, imageUrl="http...") x
    // case 2 (소셜로그인): ProfileImage(imageName=Null, imageUrl=null) x
    // case 3 (일반로그인): ProfileImage(imageName=Null, imageUrl=null) x
    // case 4 (일반로그인): ProfileImage(imageName="smile", imageUrl=null) o
    @Test
    @DisplayName("프로필 업데이트 시 불필요한 메서드 콜링이 일어나지 않는다. (입력값 imagename=null 일 때)")
    void NotCallUnnecessaryMethodWhenUpdate() {
        // given
        String nickname = "nickname";
        String imageName = null;
        Long memberId = 1L;
        memberRepository.save(MemberFixture.withFakeUser());
        memberPointRepository.save(MemberPoint.create(memberId, 1000));
        profileImageRepository.save(ProfileImage.create(memberId, imageName, "https..."));

        // when
        memberProfileService.update(MemberFixture.fixtureUpdateProfileRequest(nickname, imageName), 1L);

        // then
        assertThat(profileImageRepository.getSaveCnt()).isOne();
    }

    @Test
    @DisplayName("프로필 업데이트 시 불필요한 메서드 콜링이 일어나지 않는다. (입력값이 문자열일, 기존 존재하는 값과 같을 때)")
    void NotCallUnnecessaryMethodWhenUpdate2() {
        // given
        String nickname = "nickname";
        String imageName = "imageName";
        Long memberId = 1L;
        memberRepository.save(MemberFixture.withFakeUser());
        memberPointRepository.save(MemberPoint.create(memberId, 1000));
        profileImageRepository.save(ProfileImage.create(memberId, imageName, "https..."));

        // when
        memberProfileService.update(MemberFixture.fixtureUpdateProfileRequest(nickname, imageName), 1L);

        // then
        assertThat(profileImageRepository.getSaveCnt()).isOne();
    }

    @Test
    @DisplayName("프로필 업데이트 시 imageName이 달라질 때 업데이트가 실행된다.")
    void NotCallUnnecessaryMethodWhenUpdate3() {
        // given
        String nickname = "nickname";
        String imageName = "imageName";
        Long memberId = 1L;
        memberRepository.save(MemberFixture.withFakeUser());
        memberPointRepository.save(MemberPoint.create(memberId, 1000));
        profileImageRepository.save(ProfileImage.create(memberId, imageName, "https..."));

        // when
        memberProfileService.update(MemberFixture.fixtureUpdateProfileRequest(nickname, "newName"), 1L);

        // then
        assertThat(profileImageRepository.getSaveCnt()).isEqualTo(2);
    }


}
