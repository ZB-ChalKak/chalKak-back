package com.btb.chalKak.domain.post.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditPostRequest {

    private String content;
    private String location;

    private List<Long> styleTags;
    private List<String> hashTags;

    private boolean privacyHeight;
    private boolean privacyWeight;

    // TODO 이미지

}
