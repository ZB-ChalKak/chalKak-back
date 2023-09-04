package com.btb.chalKak.domain.like.dto;

import com.btb.chalKak.domain.like.entity.Like;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponse {

  private Long likeId;
  private Long memberId;
  private Long postId;

  public static LikeResponse fromEntity(Like like) {

    return LikeResponse.builder()
        .likeId(like.getId())
        .postId(like.getPost().getId())
        .memberId(like.getMember().getId())
        .build();
  }
}
