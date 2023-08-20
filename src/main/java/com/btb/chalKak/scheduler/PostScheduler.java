package com.btb.chalKak.scheduler;

import com.btb.chalKak.domain.post.repository.CustomPostRepository;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostScheduler {

    private final CustomPostRepository customPostRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    @Scheduled(cron = "${scheduler.cron}")
    public void deletePostViewCntCacheFromRedis() {
        Set<String> redisKeys = redisTemplate.keys("PostViewCnt*");

        if (Objects.requireNonNull(redisKeys).isEmpty()) {
            return;
        }

        for (String data : redisKeys) {
            Long postId = Long.parseLong(data.split("::")[1]);
            Long viewCnt = Long.parseLong(String.valueOf(redisTemplate.opsForValue().get(data)));
            
            // DB 데이터 반영
            customPostRepository.addViewCntFromRedis(postId, viewCnt);
            
            // 캐시 데이터 삭제
            redisTemplate.delete(data);
            redisTemplate.delete("PostViewCnt::" + postId);
        }

    }
}
