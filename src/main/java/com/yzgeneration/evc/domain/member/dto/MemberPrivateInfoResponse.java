package com.yzgeneration.evc.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberPrivateInfoResponse {
    private String accountName;
    private String accountNumber;
    private String phoneNumber;
    private String basicAddress;
    private String detailAddress;
    private Double latitude;
    private Double longitude;
}
