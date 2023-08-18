package com.btb.chalKak.domain.post.service.impl;

import static com.btb.chalKak.domain.post.type.PostStatus.PUBLIC;

import com.btb.chalKak.domain.hashTag.entity.HashTag;
import com.btb.chalKak.domain.hashTag.repository.HashTagRepository;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.post.dto.request.SavePostRequest;
import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.domain.post.repository.PostRepository;
import com.btb.chalKak.domain.post.service.PostService;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.repository.StyleTagRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final HashTagRepository hashTagRepository;
    private final StyleTagRepository styleTagRepository;

    @Override
    @Transactional
    public Long savePost(SavePostRequest request) {
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
        Post post = postRepository.save(Post.builder()
                .content(request.getContent())
                .status(PUBLIC)
                .writer(member)
                .styleTags(styleTags)
                .hashTags(hashTags)
                .build());
        
        return post.getId();
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("CustomMemberException"));
    }
}
