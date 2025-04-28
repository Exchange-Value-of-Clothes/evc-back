package com.yzgeneration.evc.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberPrivateInfoUpdate {
    private String accountName;
    private String accountNumber;
    private String phoneNumber;
}
