package com.btb.chalKak.domain.filter.service;

import com.btb.chalKak.domain.filter.dto.response.MemberFilterResponse;
import com.btb.chalKak.domain.filter.dto.response.PostFilterResponse;
import com.btb.chalKak.domain.filter.dto.response.TagFilterResponse;

import java.util.List;

public interface FilterService {
    List<MemberFilterResponse> loadUsersByKeyword(String keyword);

    List<PostFilterResponse> loadPostsByKeyword(String keyword);

    List<TagFilterResponse> loadTagsByKeyword(String keyword);
}
