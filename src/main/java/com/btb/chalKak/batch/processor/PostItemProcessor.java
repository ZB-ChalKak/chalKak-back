package com.btb.chalKak.batch.processor;

import com.btb.chalKak.common.exception.PostException;
import com.btb.chalKak.common.exception.type.ErrorCode;
import com.btb.chalKak.domain.batchpost.dto.RecommendPostBatchDTO;
import com.btb.chalKak.domain.batchpost.entity.RecommendPostBatch;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.post.dto.PostRecommendDto;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.type.StyleCategory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

import static com.btb.chalKak.common.exception.type.ErrorCode.NOT_FOUND_STYLETAG_KEYWORD;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostItemProcessor implements ItemProcessor<Post, RecommendPostBatchDTO> {

    public RecommendPostBatchDTO process(Post post) {
        PostRecommendDto postRecommendDto = PostRecommendDto.builder()
            .id(post.getId())
            .writer(post.getWriter())
            .content(post.getContent())
            .viewCount(post.getViewCount())
            .likeCount(post.getLikeCount())
            .build();

        List<Long> newStyleTagIds = post.getStyleTags().stream()
            .filter(styleTag -> (styleTag.getCategory() == StyleCategory.STYLE || styleTag.getCategory() == StyleCategory.TPO))
            .map(StyleTag::getId)
            .collect(Collectors.toList());

        return RecommendPostBatchDTO.builder()
            .id(post.getId())
            .post(postRecommendDto)
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
            .styleTagIds(newStyleTagIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")))
            .viewCount(post.getViewCount())
            .likeCount(post.getLikeCount())
            .build();
    }
}
