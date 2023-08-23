package com.btb.chalKak.domain.post.service.impl;

import static com.btb.chalKak.domain.post.type.PostStatus.PUBLIC;

import com.btb.chalKak.domain.hashTag.entity.HashTag;
import com.btb.chalKak.domain.hashTag.repository.HashTagRepository;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.post.dto.request.SavePostRequest;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.CustomPostRepository;
import com.btb.chalKak.domain.post.repository.PostRepository;
import com.btb.chalKak.domain.post.service.PostService;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.repository.StyleTagRepository;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final HashTagRepository hashTagRepository;
    private final StyleTagRepository styleTagRepository;

    private final CustomPostRepository customPostRepository;

    private final RedisTemplate<String, String> redisTemplate;

    private Authentication createMockAuthentication(Member member) {
        return new UsernamePasswordAuthenticationToken(member, null);
    }

    @Override
    @Transactional
    public Post savePost(SavePostRequest request) {
        // 1. 회원 조회
        Member member = getMemberById(request.getMemberId());

        // 2. 스타일 태그 조회
        List<StyleTag> styleTags = styleTagRepository.findAllById(request.getStyleTags());

        // 3. 해시 태그 조회 및 업로드
        List<HashTag> hashTags = new ArrayList<>();
        for (String keyword : request.getHashTags()) {
            HashTag hashTag = hashTagRepository.findByKeyword(keyword)
                    .orElse(HashTag.builder()
                            .keyword(keyword)
                            .build());

            hashTags.add(hashTag);
        }
        hashTagRepository.saveAll(hashTags);

        // 4. 게시글 저장
        return postRepository.save(Post.builder()
                .content(request.getContent())
                .status(PUBLIC)
                .writer(member)
                .viewCount(0L)
                .likeCount(0L)
                .styleTags(styleTags)
                .hashTags(hashTags)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> loadPublicPosts(Pageable pageable) {
        return customPostRepository.loadPublicPosts(pageable.getPageNumber(), pageable.getPageSize());
    }

    @Override
    @Transactional(readOnly = true)
    public Post loadPublicPostDetails(Long postId) {
        // 1. 게시글 조회
        Post post = loadPublicPostDetailsById(postId);
        
        // 2. 조회수 증가
        increasePostViewCountToRedis(postId);
        
        return post;
    }

    @Override
    @Transactional
    public void deletePost(Authentication authentication, Long postId) {
        // 1. 글쓴이 조회
        Member member = (Member) authentication.getPrincipal();

        // 2. 게시글 조회
        Post post = getPostById(postId);

        // 3. 유효성 검사(글쓴이가 본인인지 확인)
        validateWriter(member, post);
        
        // 4. 글 삭제
        post.deletePost();
        postRepository.save(post);
    }

    private void validateWriter(Member member, Post post) {
        if (!Objects.equals(member.getId(), post.getWriter().getId())) {
            throw new RuntimeException("CustomMemberException");
        }
    }

    private Post loadPublicPostDetailsById(Long postId) {
        return customPostRepository.loadPublicPostDetails(postId)
                .orElseThrow(() -> new RuntimeException("CustomMemberException"));
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("CustomPostException"));
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("CustomMemberException"));
    }

    private void increasePostViewCountToRedis(Long postId) {
        String key = "PostViewCount::" + postId;
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        if (valueOperations.get(key) == null) {
            Post post = getPostById(postId);
            valueOperations.set(
                    key,
                    String.valueOf(post.getViewCount() + 1),
                    Duration.ofMinutes(5));
        } else {
            valueOperations.increment(key);
        }
    }
}
