package com.btb.chalKak.domain.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {

    USER("ROLE_USER", "일반회원"),
    ADMIN("ROLE_ADMIN", "어드민"),
    ;

    private final String role;
    private final String description;
}
