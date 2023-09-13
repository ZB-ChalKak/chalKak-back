package com.btb.chalKak.domain.batchpost.dto;

import com.btb.chalKak.domain.post.dto.PostRecommendDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendPostBatchDTO {
  private Long id;
  private PostRecommendDto post;
  private Long weatherId;
  private Long seasonId;
  private String styleTagIds;
  private Long viewCount;
  private Long likeCount;

  public List<Long> getStyleTagIdsList() {
    if (styleTagIds == null || styleTagIds.isEmpty()) {
      return new ArrayList<>();
    }
    return Arrays.stream(styleTagIds.split(","))
        .map(Long::valueOf)
        .collect(Collectors.toList());
  }

}