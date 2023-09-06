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

    private Long id;
    private String keyword;

    public static HashTagFilterDto fromEntity(HashTag hashTag){
        return HashTagFilterDto.builder()
                .id(hashTag.getId())
                .keyword(hashTag.getKeyword())
                .build();
    }
}