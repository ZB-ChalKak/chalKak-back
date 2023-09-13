package com.btb.chalKak.domain.batchpost.service;

import static com.btb.chalKak.common.exception.type.ErrorCode.NOT_FOUND_STYLETAG_KEYWORD;

import com.btb.chalKak.common.exception.PostException;
import com.btb.chalKak.domain.batchpost.entity.RecommendPostBatch;
import com.btb.chalKak.domain.batchpost.repository.RecommendPostBatchRepository;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.type.StyleCategory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendPostBatchService {

  private final RecommendPostBatchRepository recommendPostBatchRepository;


  public RecommendPostBatch saveAll(Post post){

      return recommendPostBatchRepository.save(process(post));
  }

  public RecommendPostBatch process(Post post) {

    List<Long> newStyleTagIds = post.getStyleTags().stream()
        .filter(styleTag -> styleTag.getCategory() == StyleCategory.STYLE)
        .map(StyleTag::getId)
        .collect(Collectors.toList());

    String newStyleTagIdsString = newStyleTagIds.stream()
        .map(Object::toString)
        .collect(Collectors.joining(","));

    return  RecommendPostBatch.builder()
        .id(post.getId())
//        .post(post)
        .weatherId(post.getStyleTags().stream()
            .filter(styleTag -> styleTag.getCategory() == StyleCategory.WEATHER)
            .map(StyleTag::getId)
            .findFirst()
            .orElseThrow(()->new PostException(NOT_FOUND_STYLETAG_KEYWORD))
        )
        .seasonId(post.getStyleTags().stream()
            .filter(styleTag -> styleTag.getCategory() == StyleCategory.SEASON)
            .map(StyleTag::getId)
            .findFirst()
            .orElseThrow(()->new PostException(NOT_FOUND_STYLETAG_KEYWORD)))
        .styleTagIds(newStyleTagIdsString)
        .viewCount(post.getViewCount())
        .likeCount(post.getLikeCount())
        .build();
  }
}
