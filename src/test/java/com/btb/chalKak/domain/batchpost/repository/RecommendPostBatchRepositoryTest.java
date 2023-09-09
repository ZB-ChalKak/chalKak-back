package com.btb.chalKak.domain.batchpost.repository;

import com.btb.chalKak.common.exception.PostException;
import com.btb.chalKak.domain.batchpost.entity.RecommendPostBatch;
import com.btb.chalKak.domain.post.repository.PostRepository;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.type.StyleCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static com.btb.chalKak.common.exception.type.ErrorCode.NOT_FOUND_STYLETAG_KEYWORD;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RecommendPostBatchRepositoryTest {

    @Autowired
    private RecommendPostBatchRepository recommendPostBatchRepository;

    @Autowired
    private PostRepository postRepository;
    @Test
    public void testSaveAllSimple() {
        // Arrange
        RecommendPostBatch entity1 = RecommendPostBatch.builder()
                .id(2L)
//                .post(postRepository.findById(2L).orElse(null))
                .weatherId(2L)
                .seasonId(2L)
                .styleTagIds("1,1,2")
                .viewCount(20L)
                .likeCount(20L)
                .build(); // fill in constructor arguments

        RecommendPostBatch entity2 = RecommendPostBatch.builder()
                .id(2L)
//                .post(postRepository.findById(2L).orElse(null))
                .weatherId(2L)
                .seasonId(2L)
                .styleTagIds("1,1,2")
                .viewCount(20L)
                .likeCount(20L)
                .build(); // fill in constructor arguments

        List<RecommendPostBatch> entitiesToSave = Arrays.asList(entity1, entity2);

        // Act
        recommendPostBatchRepository.saveAll(entitiesToSave);

        // Assert
        List<RecommendPostBatch> retrievedEntities = recommendPostBatchRepository.findAll();
        assertEquals(2, retrievedEntities.size());
        // Add more assertions to check the actual saved data
    }

}