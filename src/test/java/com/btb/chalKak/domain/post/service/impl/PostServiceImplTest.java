package com.btb.chalKak.domain.post.service.impl;

import static com.btb.chalKak.domain.post.type.PostStatus.PUBLIC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import com.btb.chalKak.domain.hashTag.entity.HashTag;
import com.btb.chalKak.domain.hashTag.repository.HashTagRepository;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.post.dto.request.SavePostRequest;
import com.btb.chalKak.domain.post.entity.Post;
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
                .content("TEST")
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
}