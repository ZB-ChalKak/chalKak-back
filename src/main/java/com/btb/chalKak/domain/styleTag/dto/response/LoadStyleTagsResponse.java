package com.btb.chalKak.domain.styleTag.dto.response;

import com.btb.chalKak.domain.styleTag.dto.StyleTagDto;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoadStyleTagsResponse {

    List<StyleTagDto> styleTags;

    public static LoadStyleTagsResponse fromEntities(List<StyleTag> styleTags) {
        return LoadStyleTagsResponse.builder()
                .styleTags(styleTags.stream()
                        .map(StyleTagDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();

    }

}
