package com.btb.chalKak.domain.styleTag.dto;

import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.type.StyleCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StyleTagDto {

    private Long id;
    private StyleCategory category;
    private String keywordImg;
    private String keyword;

    public static StyleTagDto fromEntity(StyleTag styleTag) {
        return StyleTagDto.builder()
                .id(styleTag.getId())
                .category(styleTag.getCategory())
                .keywordImg(styleTag.getKeywordImg())
                .keyword(styleTag.getKeyword())
                .build();
    }

}