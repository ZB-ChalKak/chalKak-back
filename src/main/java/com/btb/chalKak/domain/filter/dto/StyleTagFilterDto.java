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

    private Long styleTagId;
    private String keyword;

    public static StyleTagFilterDto fromEntity(StyleTag styleTag){
        return StyleTagFilterDto.builder()
                .styleTagId(styleTag.getId())
                .keyword(styleTag.getKeyword())
                .build();
    }
}
