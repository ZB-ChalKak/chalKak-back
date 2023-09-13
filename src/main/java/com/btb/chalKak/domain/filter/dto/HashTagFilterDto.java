package com.btb.chalKak.domain.filter.dto;

import com.btb.chalKak.domain.hashTag.entity.HashTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HashTagFilterDto {

    private Long tagId;
    private String keyword;

    public static HashTagFilterDto fromEntity(HashTag hashTag){
        return HashTagFilterDto.builder()
                .tagId(hashTag.getId())
                .keyword(hashTag.getKeyword())
                .build();
    }
}
