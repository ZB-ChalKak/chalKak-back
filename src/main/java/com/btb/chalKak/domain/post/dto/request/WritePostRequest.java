package com.btb.chalKak.domain.post.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WritePostRequest {
    
    private String content;

    private List<Long> styleTags;
    private List<String> hashTags;

    private boolean privacyHeight;
    private boolean privacyWeight;

    private String location;
    
    // TODO 이미지 작업 추가 필요

}
