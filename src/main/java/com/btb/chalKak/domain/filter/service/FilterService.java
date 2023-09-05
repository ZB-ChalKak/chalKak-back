package com.btb.chalKak.domain.filter.service;

import com.btb.chalKak.domain.filter.dto.response.MemberFilterResponse;
import com.btb.chalKak.domain.filter.dto.response.PostFilterResponse;
import com.btb.chalKak.domain.filter.dto.response.TagFilterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FilterService {
    Page<MemberFilterResponse> loadUsersByKeyword(String keyword, Pageable pageable);

    Page<PostFilterResponse> loadPostsByKeyword(String keyword, Long length, Pageable pageable);

    TagFilterResponse loadTagsByKeyword(String keyword, Pageable pageable);
}
