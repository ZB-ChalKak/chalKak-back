package com.btb.chalKak.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditPost {

    private String content;
    private String location;

    private boolean privacyHeight;
    private boolean privacyWeight;

    // TODO 이미지
}
