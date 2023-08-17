package com.btb.chalKak.domain.post.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostStatus {

    PUBLIC("공개"),
    PRIVATE("비공개"),
    DELETED("삭제"),

    ;

    private final String description;
}
