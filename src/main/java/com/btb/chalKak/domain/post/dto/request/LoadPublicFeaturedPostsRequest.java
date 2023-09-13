package com.btb.chalKak.domain.post.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoadPublicFeaturedPostsRequest {

    private double height;
    private double weight;

    private List<Long> styleTagIds;
}
