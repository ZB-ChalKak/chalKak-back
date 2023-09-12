package com.btb.chalKak.batch.reader;

import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.PostRepository;
import com.btb.chalKak.domain.weather.service.Impl.WeatherServiceImpl;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostItemReader implements ItemReader<Post> {

    private final PostRepository postRepository;

    private final WeatherServiceImpl weatherService;

    private Iterator<Post> postIterator;

    private final Long minCount = 1L;

    public Post read() {
        if (postIterator == null) {
            // 오늘 07시 기준 우리나라의 계절 and 일정 이상의 view + like count
            Long seasonId = weatherService.weatherToSeasonId(LocalDate.now());

            // 첫 번째 쿼리: 조건에 맞는 Post의 ID들을 가져온다.
            List<Post> posts = postRepository.findPostsAndSeasonIdAndFetch(seasonId, minCount);

            List<Long> postIds = new ArrayList<>();

            for (Post post : posts) {
                postIds.add(post.getId());
            }

            // 두 번째 쿼리: ID 리스트를 사용하여 Post와 Writer를 Eager 로딩한다.
            List<Post> resultPosts = postRepository.findPostsWithWritersByIds(postIds);

            postIterator = resultPosts.iterator();
        }

        if (postIterator.hasNext()) {
            return postIterator.next();
        } else {
            return null;
        }
    }
}
