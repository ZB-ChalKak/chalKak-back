package com.btb.chalKak.domain.styleTag.dto;

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
    private String keyword;

}