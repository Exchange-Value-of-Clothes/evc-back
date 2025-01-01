package com.yzgeneration.evc.member.infrastructure;

import com.yzgeneration.evc.member.enums.MemberRole;
import com.yzgeneration.evc.member.enums.MemberStatus;
import com.yzgeneration.evc.member.model.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MemberPrivateInformationEntity memberPrivateInformationEntity;

    @Embedded
    private MemberAuthenticationInformationEntity memberAuthenticationInformationEntity;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Builder
    public MemberEntity(Long id, MemberPrivateInformationEntity memberPrivateInformationEntity,
                        MemberAuthenticationInformationEntity memberAuthenticationInformationEntity,
                        MemberRole memberRole, MemberStatus memberStatus) {
        this.id = id;
        this.memberPrivateInformationEntity = memberPrivateInformationEntity;
        this.memberAuthenticationInformationEntity = memberAuthenticationInformationEntity;
        this.memberRole = memberRole;
        this.memberStatus = memberStatus;
    }

    public static MemberEntity from(Member member) {
        return MemberEntity.builder()
                .memberPrivateInformationEntity(MemberPrivateInformationEntity.from(member.getMemberPrivateInformation()))
                .memberAuthenticationInformationEntity(MemberAuthenticationInformationEntity.from(member.getMemberAuthenticationInformation()))
                .memberRole(member.getMemberRole())
                .memberStatus(member.getMemberStatus())
                .build();
    }

    public Member toModel() {
        return Member.builder()
                .id(id)
                .memberPrivateInformation(memberPrivateInformationEntity.toModel())
                .memberAuthenticationInformation(memberAuthenticationInformationEntity.toModel())
                .memberRole(memberRole)
                .memberStatus(memberStatus)
                .build();

    }
}