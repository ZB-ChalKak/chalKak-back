package com.btb.chalKak.domain.post.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SavePostRequest {
    
    private Long memberId;
    private String content;
    private List<Long> styleTags;
    private List<String> hashTags;
    
    // TODO 이미지 작업 추가 필요

}
