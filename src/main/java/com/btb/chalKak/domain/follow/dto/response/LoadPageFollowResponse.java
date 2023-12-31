package com.btb.chalKak.domain.follow.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LoadPageFollowResponse {

    private int currentPage;
    private int totalPages;

    private long totalElements;

    private List<FollowerResponse> followerResponses;

}
