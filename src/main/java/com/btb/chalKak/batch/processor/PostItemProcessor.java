package com.btb.chalKak.batch.processor;

import com.btb.chalKak.common.exception.PostException;
import com.btb.chalKak.common.exception.type.ErrorCode;
import com.btb.chalKak.domain.batchpost.entity.RecommendPostBatch;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.type.StyleCategory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.btb.chalKak.common.exception.type.ErrorCode.NOT_FOUND_STYLETAG_KEYWORD;

@Component
public class PostItemProcessor implements ItemProcessor<Post, RecommendPostBatch> {

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
                .post(post)
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