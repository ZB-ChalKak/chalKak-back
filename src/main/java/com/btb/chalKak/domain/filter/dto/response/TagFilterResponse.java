package com.btb.chalKak.domain.filter.dto.response;

import com.btb.chalKak.domain.filter.dto.HashTagFilterDto;
import com.btb.chalKak.domain.filter.dto.StyleTagFilterDto;
import com.btb.chalKak.domain.filter.type.TagType;
import com.btb.chalKak.domain.hashTag.entity.HashTag;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
public class TagFilterResponse {

    private List<StyleTagFilterDto> styleTags;
    private List<HashTagFilterDto> hashTags;
}
