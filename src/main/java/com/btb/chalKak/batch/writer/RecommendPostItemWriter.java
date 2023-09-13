package com.btb.chalKak.batch.writer;

import com.btb.chalKak.domain.batchpost.dto.RecommendPostBatchDTO;
import com.btb.chalKak.domain.batchpost.entity.RecommendPostBatch;
import com.btb.chalKak.domain.batchpost.repository.RecommendPostBatchRepository;
import com.btb.chalKak.domain.post.entity.Post;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RecommendPostItemWriter implements ItemWriter<RecommendPostBatchDTO> {


    private final RecommendPostBatchRepository recommendPostBatchRepository;

    @Override
    public void write(List<? extends RecommendPostBatchDTO> dtos) {
        List<RecommendPostBatch> entities = dtos.stream().map(dto -> {
            // Using Builder to create a Post entity
            Post post = Post.builder()
                .id(dto.getPost().getId())
                .writer(dto.getPost().getWriter())
                .content(dto.getPost().getContent())
                .viewCount(dto.getPost().getViewCount())
                .likeCount(dto.getPost().getLikeCount())
                .build();

            // Using Builder to create a RecommendPostBatch entity
            RecommendPostBatch entity = RecommendPostBatch.builder()
                .id(dto.getId())
//                .post(post)
                .weatherId(dto.getWeatherId())
                .seasonId(dto.getSeasonId())
                .viewCount(dto.getViewCount())
                .likeCount(dto.getLikeCount())
                .build();

            entity.updateStyleTagIdsList(dto.getStyleTagIdsList());

            return entity;
        }).collect(Collectors.toList());

        recommendPostBatchRepository.saveAll(entities);
    }
}
