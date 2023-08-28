package com.btb.chalKak.domain.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberProvider {

    CHALKAK("찰칵"),
    GOOGLE("구글"),

    ;

    private final String description;

}

