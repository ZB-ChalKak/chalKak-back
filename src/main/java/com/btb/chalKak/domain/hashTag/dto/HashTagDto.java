package com.btb.chalKak.domain.hashTag.dto;

import com.btb.chalKak.domain.hashTag.entity.HashTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HashTagDto {

    private Long id;
    private String keyword;

    public static HashTagDto fromEntity(HashTag hashTag) {
        return HashTagDto.builder()
                .id(hashTag.getId())
                .keyword(hashTag.getKeyword())
                .build();
    }

}