package com.btb.chalKak.scheduler;

import com.btb.chalKak.domain.post.repository.CustomPostRepository;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostScheduler {

    private final CustomPostRepository customPostRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    @Scheduled(cron = "${scheduler.cron}")
    public void deletePostViewCountCacheFromRedis() {
        Set<String> postViewCountKeys = redisTemplate.keys("PostViewCount*");

        if (Objects.requireNonNull(postViewCountKeys).isEmpty()) {
            return;
        }

        for (String postViewCountKey : postViewCountKeys) {
            Long postId = getPostIdByRedisKey(postViewCountKey);
            Long viewCount = Long.parseLong(String.valueOf(redisTemplate.opsForValue().get(postViewCountKey)));
            
            // DB 데이터 반영
            customPostRepository.addViewCountFromRedis(postId, viewCount);
            
            // 캐시 데이터 삭제
            redisTemplate.delete(postViewCountKey);
            redisTemplate.delete("PostViewCount::" + postId);
        }

    }

    private long getPostIdByRedisKey(String postViewCountKey) {
        return Long.parseLong(postViewCountKey.split("::")[1]);
    }
}
