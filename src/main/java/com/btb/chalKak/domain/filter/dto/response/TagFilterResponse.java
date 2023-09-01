package com.btb.chalKak.domain.filter.dto.response;

import com.btb.chalKak.domain.filter.type.TagType;
import com.btb.chalKak.domain.hashTag.entity.HashTag;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TagFilterResponse {

    private Long tagId;
    private TagType tagType;
    private String keyword;

    public static TagFilterResponse fromHashTagEntity(HashTag hashTag){
        return TagFilterResponse.builder()
                .tagId(hashTag.getId())
                .tagType(TagType.HASHTAG)
                .keyword(hashTag.getKeyword())
                .build();
    }

    public static TagFilterResponse fromStyleTagEntity(StyleTag styleTag){
        return TagFilterResponse.builder()
                .tagId(styleTag.getId())
                .tagType(TagType.STYLETAG)
                .keyword(styleTag.getKeyword())
                .build();
    }
}
