package com.btb.chalKak.batch.writer;

import com.btb.chalKak.domain.batchpost.entity.RecommendPostBatch;
import com.btb.chalKak.domain.batchpost.repository.RecommendPostBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecommendPostItemWriter implements ItemWriter<RecommendPostBatch> {

    private final RecommendPostBatchRepository recommendPostBatchRepository;

    public void write(List<? extends RecommendPostBatch> list) {
        recommendPostBatchRepository.saveAll(list);
    }
}
