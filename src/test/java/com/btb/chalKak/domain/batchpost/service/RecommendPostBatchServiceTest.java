package com.btb.chalKak.domain.batchpost.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.btb.chalKak.common.exception.PostException;
import com.btb.chalKak.common.exception.type.ErrorCode;
import com.btb.chalKak.domain.batchpost.entity.RecommendPostBatch;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class RecommendPostBatchServiceTest {


  @Autowired
  private RecommendPostBatchService recommendPostBatchService;

  @Autowired
  private PostRepository postRepository;

  @Test
  @Transactional
  void saveAll() {

    Post post = postRepository.findById(7L)
        .orElseThrow(()-> new PostException(ErrorCode.INVALID_POST_ID));


    RecommendPostBatch recommendPostBatch = recommendPostBatchService.saveAll(post);

    assertEquals(post.getId(),recommendPostBatch.getPost().getId());

  }
}