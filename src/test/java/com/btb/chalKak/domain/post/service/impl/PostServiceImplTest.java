package com.btb.chalKak.domain.post.service.impl;

import static com.btb.chalKak.domain.post.type.PostStatus.PUBLIC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.btb.chalKak.domain.hashTag.entity.HashTag;
import com.btb.chalKak.domain.hashTag.repository.HashTagRepository;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.post.dto.request.SavePostRequest;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.CustomPostRepository;
import com.btb.chalKak.domain.post.repository.PostRepository;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.repository.StyleTagRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {
    
    @Mock
    private PostRepository postRepository;
    
    @Mock
    private MemberRepository memberRepository;
    
    @Mock
    private HashTagRepository hashTagRepository;
    
    @Mock
    private StyleTagRepository styleTagRepository;

    @Mock
    private CustomPostRepository customPostRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    @DisplayName("게시글 저장 - 성공")
    void savePost() {

        // given
        Long memberId = 20L;
        Member member = Member.builder()
                .id(memberId)
                .build();

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));

        List<String> styleTagKeywords = List.of("오늘의룩", "여름룩");
        List<StyleTag> styleTags = styleTagKeywords.stream()
                .map(keyword -> StyleTag.builder()
                        .id(10L)
                        .keyword(keyword)
                        .build())
                .collect(Collectors.toList());

        List<Long> styleTagIds = styleTags.stream()
                .map(StyleTag::getId)
                .collect(Collectors.toList());

        given(styleTagRepository.findAllById(anyList()))
                .willReturn(styleTags);

        List<String> hashTagKeywords = List.of("한여름코디", "오뭐신");
        List<HashTag> hashTags = hashTagKeywords.stream()
                .map(keyword -> HashTag.builder()
                        .id(10L)
                        .keyword(keyword)
                        .build())
                .collect(Collectors.toList());

        given(hashTagRepository.saveAll(anyList()))
                .willReturn(hashTags);

        SavePostRequest request = SavePostRequest.builder()
                .memberId(memberId)
                .styleTags(styleTagIds)
                .hashTags(hashTagKeywords)
                .content("출석합니다.")
                .build();

        Post post = Post.builder()
                .id(15L)
                .content(request.getContent())
                .status(PUBLIC)
                .writer(member)
                .styleTags(styleTags)
                .hashTags(hashTags)
                .build();

        given(postRepository.save(any(Post.class)))
                .willReturn(post);

        // when
        Post saved = postService.savePost(request);

        // then
        assertEquals(15L, saved.getId());
    }

    @Test
    @DisplayName("게시글 조회 - 성공")
    void loadPublicPosts() {

        // given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Member writer = Member.builder()
                .id(20L)
                .build();

        List<Post> posts = List.of(
                Post.builder()
                        .id(1L)
                        .content("출석합니다.")
                        .status(PUBLIC)
                        .writer(writer)
                        .build()
        );

        Page<Post> expected = new PageImpl<>(posts, pageable, posts.size());

        given(customPostRepository.loadPublicPosts(any(Integer.class), any(Integer.class)))
                .willReturn(expected);

        // when
        Page<Post> result = postService.loadPublicPosts(pageable);

        // then
        assertEquals(expected.getTotalElements(), result.getTotalElements());
        assertEquals(expected.getContent().get(0).getId(), result.getContent().get(0).getId());
    }

    @Test
    @DisplayName("게시글 상세 조회 - 성공")
    void loadPublicPostDetails() {

        // given
        Long postId = 15L;
        Member writer = Member.builder()
                .id(1L)
                .build();

        Post expected = Post.builder()
                .id(postId)
                .content("출석합니다.")
                .status(PUBLIC)
                .viewCount(0L)
                .writer(writer)
                .build();

        given(customPostRepository.loadPublicPostDetails(postId))
                .willReturn(Optional.of(expected));

        given(postRepository.findById(anyLong()))
                .willReturn(Optional.of(expected));

        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        given(redisTemplate.opsForValue())
                .willReturn(valueOperations);

        // when
        Post post = postService.loadPublicPostDetails(postId);

        // then
        assertEquals(expected.getId(), post.getId());
        assertEquals(expected.getContent(), post.getContent());
        assertEquals(expected.getStatus(), post.getStatus());
        assertEquals(expected.getViewCount(), post.getViewCount());
        assertEquals(expected.getWriter().getId(), post.getWriter().getId());
    }

}