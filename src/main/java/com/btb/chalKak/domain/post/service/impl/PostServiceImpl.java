package com.btb.chalKak.domain.post.service.impl;

import static com.btb.chalKak.common.response.type.ErrorCode.INVALID_POST_ID;
import static com.btb.chalKak.common.response.type.ErrorCode.LOAD_MEMBER_FAILED;
import static com.btb.chalKak.common.response.type.ErrorCode.MISMATCH_WRITER;

import com.btb.chalKak.common.exception.MemberException;
import com.btb.chalKak.common.exception.PostException;
import com.btb.chalKak.common.security.customUser.CustomUserDetails;
import com.btb.chalKak.domain.follow.repository.FollowRepository;
import com.btb.chalKak.domain.hashTag.entity.HashTag;
import com.btb.chalKak.domain.hashTag.repository.HashTagRepository;
import com.btb.chalKak.domain.like.repository.LikeRepository;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.post.dto.EditPost;
import com.btb.chalKak.domain.post.dto.request.EditPostRequest;
import com.btb.chalKak.domain.post.dto.request.LoadPublicFeaturedPostsRequest;
import com.btb.chalKak.domain.post.dto.request.WritePostRequest;
import com.btb.chalKak.domain.post.entity.Post;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;
    private final StyleTagRepository styleTagRepository;

    private final LikeRepository likeRepository;
    private final FollowRepository followRepository;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public Post write(Authentication authentication, WritePostRequest request) {
        // 1. 회원 조회
        Member member = getMemberByAuthentication(authentication);
        validateAuthenticated(member);

        // 2. 스타일 태그 조회
        List<StyleTag> styleTags = styleTagRepository.findAllById(request.getStyleTags());

        // 3. 해시 태그 조회 및 업로드
        List<HashTag> hashTags = getHashTagsByKeywords(request.getHashTags());
        hashTagRepository.saveAll(hashTags);

        // 4. 게시글 저장
        return postRepository.save(Post.builder()
                .content(request.getContent())
                .writer(member)
                .location(request.getLocation())
                .privacyHeight(request.isPrivacyHeight())
                .privacyWeight(request.isPrivacyWeight())
                .styleTags(styleTags)
                .hashTags(hashTags)
                .build());
    }

    @Override
    @Transactional
    public Post edit(Authentication authentication, Long postId, EditPostRequest request) {
        // 1. 회원 조회
        Member member = getMemberByAuthentication(authentication);
        validateAuthenticated(member);

        // 2. 게시글 조회
        Post post = getPostById(postId);

        // 3. 유효성 검사(글쓴이가 본인인지 확인)
        validateWriterOfPost(member, post);

        // 4. 편집된 스타일 태그 업데이트
        List<StyleTag> editedStyleTags = styleTagRepository.findAllById(request.getStyleTags());
        post.updateStyleTags(editedStyleTags);

        // 5. 편집된 해시 태그 업데이트
        List<HashTag> editedHashTags = getHashTagsByKeywords(request.getHashTags());
        post.updateHashTags(editedHashTags);
        hashTagRepository.saveAll(editedHashTags);

        // 6. 게시글 저장
        return postRepository.save(post.edit(EditPost.builder()
                .privacyHeight(request.isPrivacyHeight())
                .privacyWeight(request.isPrivacyWeight())
                .content(request.getContent())
                .location(request.getLocation())
                .build()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> loadPublicPostsOrderByDesc(Pageable pageable) {
        return postRepository.loadPublicPostsOrderByDesc(pageable.getPageNumber(), pageable.getPageSize());
    }

    @Override
    @Transactional(readOnly = true)
    public Post loadPublicPostDetails(Authentication authentication, Long postId) {
        // 1. 회원 조회
        Member member = getMemberByAuthentication(authentication);

        // 2. 게시글 조회
        Post post = loadPublicPostDetailsById(postId);

        // 3. 좋아요 및 팔로잉 여부 업데이트
        boolean isLiked = false;
        boolean isFollowing = false;
        if (member != null) {
            isLiked = likeRepository.existsByMemberIdAndPostId(member.getId(), postId);
            isFollowing = followRepository.existsByFollowingIdAndFollowerId(member.getId(), post.getWriter().getId());
        }

        post.updateIsFollowingAndIsLiked(isFollowing, isLiked);

        // 4. 조회수 증가
        increasePostViewCountToRedis(postId);

        return post;
    }

    @Override
    @Transactional
    public void delete(Authentication authentication, Long postId) {
        // 1. 글쓴이 조회
        Member member = getMemberByAuthentication(authentication);

        // 2. 게시글 조회
        Post post = getPostById(postId);

        // 3. 유효성 검사(글쓴이가 본인인지 확인)
        validateWriterOfPost(member, post);

        // 4. 글 삭제
        postRepository.save(post.delete());
    }

//    @Override
//    @Transactional(readOnly = true)
//    public Page<Post> loadPublicFeaturedPostsByKeywords(
//            Pageable pageable,
//            LoadPublicFeaturedPostsRequest request
//    ) {
//        // 1. 키워드로 조회
//
//        // 2. 좋아요, 팔로우, 조회수 수 및 자신의 성별 순서 정렬(추천)
//
//        return null;
//    }

    private void validateWriterOfPost(Member member, Post post) {
        if (!Objects.equals(member.getId(), post.getWriter().getId())) {
            throw new PostException(MISMATCH_WRITER);
        }
    }

    private void validateAuthenticated(Member member) {
        if (member == null) {
            throw new MemberException(LOAD_MEMBER_FAILED);
        }
    }

    private Post loadPublicPostDetailsById(Long postId) {
        return postRepository.loadPublicPostDetails(postId)
                .orElseThrow(() -> new PostException(INVALID_POST_ID));
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(INVALID_POST_ID));
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

    private Member getMemberByAuthentication(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getMember();
    }

    private List<HashTag> getHashTagsByKeywords(List<String> keywords) {
        List<HashTag> hashTags = new ArrayList<>();
        for (String keyword : keywords) {
            HashTag hashTag = hashTagRepository.findByKeyword(keyword)
                    .orElse(HashTag.builder()
                            .keyword(keyword)
                            .build());

            hashTags.add(hashTag);
        }
        return hashTags;
    }

}
