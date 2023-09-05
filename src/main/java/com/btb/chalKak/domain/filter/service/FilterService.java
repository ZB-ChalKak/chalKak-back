package com.btb.chalKak.domain.filter.service;

import com.btb.chalKak.domain.filter.dto.HashTagFilterDto;
import com.btb.chalKak.domain.filter.dto.StyleTagFilterDto;
import com.btb.chalKak.domain.filter.dto.response.MemberFilterResponse;
import com.btb.chalKak.domain.filter.dto.response.PostFilterResponse;
import com.btb.chalKak.domain.filter.dto.response.TagFilterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FilterService {
    List<MemberFilterResponse> loadUsersByKeyword(String keyword, Pageable pageable);

    List<PostFilterResponse> loadPostsByKeyword(String keyword, Long length, Pageable pageable);

    List<HashTagFilterDto> loadHashTagsByKeyword(String keyword, Pageable pageable);

    List<StyleTagFilterDto> loadStyleTagsByKeyword(String keyword, Pageable pageable);
}
