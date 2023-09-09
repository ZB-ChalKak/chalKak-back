package com.btb.chalKak.batch.reader;

import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.PostRepository;
import com.btb.chalKak.domain.weather.service.Impl.WeatherServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class PostItemReader implements ItemReader<Post> {

    private final PostRepository postRepository;

    private final WeatherServiceImpl weatherService;

    private Iterator<Post> postIterator;

    private final Long minCount = 1L;

    public Post read() {

        Long seasonId = weatherService.weatherToSeasonId(LocalDate.now());

        if (postIterator == null) {
            // 오늘 07시 기준 우리나라의 계절 and 일정 이상의 view + like count
            postIterator = postRepository.findPostsAndSeasonId(seasonId, minCount)
                    .iterator();
        }

        if (postIterator.hasNext()) {
            return postIterator.next();
        } else {
            return null;
        }
    }
}
