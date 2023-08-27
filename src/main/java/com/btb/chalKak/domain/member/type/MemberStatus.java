package com.btb.chalKak.domain.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberStatus {

    ACTIVE("활성화"),
    INACTIVE("비활성화"),
    WITHDRAWAL("탈퇴"),
    BLOCKED("정지"),

    ;

    private final String description;

}
