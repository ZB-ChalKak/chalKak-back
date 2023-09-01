package com.btb.chalKak.domain.like.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LoadPageLikeResponse {
    private int totalPages;
    private int currentPage;
    private long totalElements;
    private List<LikerResponse> likerResponses;
}