package com.btb.chalKak.domain.filter.service.impl;

import com.btb.chalKak.domain.filter.dto.response.MemberFilterResponse;
import com.btb.chalKak.domain.filter.dto.response.PostFilterResponse;
import com.btb.chalKak.domain.filter.dto.response.TagFilterResponse;
import com.btb.chalKak.domain.filter.service.FilterService;
import com.btb.chalKak.domain.hashTag.repository.HashTagRepository;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.post.repository.PostRepository;
import com.btb.chalKak.domain.styleTag.repository.StyleTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;
    private final StyleTagRepository styleTagRepository;

    @Override
    @Transactional
    public List<MemberFilterResponse> loadUsersByKeyword(String keyword) {
        return memberRepository.findAllByNicknameContaining(keyword)
                .stream()
                .map(member -> MemberFilterResponse.fromEntity(member))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PostFilterResponse> loadPostsByKeyword(String keyword) {
        return postRepository.findAllByContentContaining(keyword)
                .stream()
                .map(post -> PostFilterResponse.fromEntity(post))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    // TODO : 태그를 종류 별로 나누어 받을 건지, 합쳐서 받을 건지 논의 필요
    public List<TagFilterResponse> loadTagsByKeyword(String keyword) {
        List<TagFilterResponse> tags = new ArrayList<>();

        tags.addAll(
            hashTagRepository.findAllByKeywordContaining(keyword)
                    .stream()
                    .map(hashTag -> TagFilterResponse.fromHashTagEntity(hashTag))
                    .collect(Collectors.toList())
        );

        tags.addAll(
            styleTagRepository.findAllByKeywordContaining(keyword)
                    .stream()
                    .map(styleTag -> TagFilterResponse.fromStyleTagEntity(styleTag))
                    .collect(Collectors.toList())
        );

        return tags;
    }
}
