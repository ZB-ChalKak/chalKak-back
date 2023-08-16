package com.btb.chalKak.domain.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {

    USER("ROLE_USER", "일반회원")
    ;

    private final String role;
    private final String description;
}
