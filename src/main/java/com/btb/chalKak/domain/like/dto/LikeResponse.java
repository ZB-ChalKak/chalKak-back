package com.btb.chalKak.domain.like.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponse {

  private Long likeId;
  private Long memberId;
  private Long postId;

}
