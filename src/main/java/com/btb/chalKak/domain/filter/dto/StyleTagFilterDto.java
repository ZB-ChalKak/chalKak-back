package com.btb.chalKak.domain.filter.dto;

import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StyleTagFilterDto {

    private Long tagId;
    private String keyword;

    public static StyleTagFilterDto fromEntity(StyleTag styleTag){
        return StyleTagFilterDto.builder()
                .tagId(styleTag.getId())
                .keyword(styleTag.getKeyword())
                .build();
    }
}
