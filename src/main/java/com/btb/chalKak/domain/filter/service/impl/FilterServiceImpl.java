package com.btb.chalKak.domain.filter.service.impl;

import com.btb.chalKak.common.exception.FilterException;
import com.btb.chalKak.common.exception.MemberException;
import com.btb.chalKak.domain.filter.dto.HashTagFilterDto;
import com.btb.chalKak.domain.filter.dto.StyleTagFilterDto;
import com.btb.chalKak.domain.filter.dto.response.MemberFilterResponse;
import com.btb.chalKak.domain.filter.dto.response.PostFilterResponse;
import com.btb.chalKak.domain.filter.service.FilterService;
import com.btb.chalKak.domain.hashTag.repository.HashTagRepository;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.post.repository.PostRepository;
import com.btb.chalKak.domain.styleTag.repository.StyleTagRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Collectors;

import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_NICKNAME;
import static com.btb.chalKak.common.exception.type.ErrorCode.INVALID_PREVIEW_CONTENT_LENGTH;

@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;
    private final StyleTagRepository styleTagRepository;

    @Override
    @Transactional
    public List<MemberFilterResponse> loadUsersByKeyword(String keyword, Pageable pageable) {
        return memberRepository.findAllByNicknameContaining(getDecodingUrl(keyword), pageable).getContent()
                .stream()
                .map(member -> MemberFilterResponse.fromEntity(member))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PostFilterResponse> loadPostsByKeyword(String keyword, Long maxLength, Pageable pageable) {
        return postRepository.findAllByContentContaining(getDecodingUrl(keyword), pageable).getContent()
                .stream()
                .map(post -> PostFilterResponse.builder()
                        .postId(post.getId())
                        .content(post.getContent())
                        .previewContent(getPreviewContentByContent(post.getContent(), keyword, maxLength))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<HashTagFilterDto> loadHashTagsByKeyword(String keyword, Pageable pageable) {
        return hashTagRepository.findAllByKeywordContaining(keyword, pageable).getContent()
                .stream()
                .map(hashTag -> HashTagFilterDto.builder()
                        .hashTagId(hashTag.getId())
                        .keyword(hashTag.getKeyword())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<StyleTagFilterDto> loadStyleTagsByKeyword(String keyword, Pageable pageable) {
        return styleTagRepository.findAllByKeywordContaining(keyword, pageable).getContent()
                .stream()
                .map(styleTag -> StyleTagFilterDto.builder()
                        .styleTagId(styleTag.getId())
                        .keyword(styleTag.getKeyword())
                        .build())
                .collect(Collectors.toList());
    }

    private String getDecodingUrl(String urlString){
        try {
            return URLDecoder.decode(urlString, "UTF-8");
        }catch(Exception e) {
            throw new MemberException(INVALID_NICKNAME);
        }
    }

    private String getPreviewContentByContent(String content, String keyword, Long length){
        // substring이 long값을 받지 못해 임시 방편
        if(length > Integer.MAX_VALUE || length < 0){
            throw new FilterException(INVALID_PREVIEW_CONTENT_LENGTH);
        }

        int maxLength = length.intValue();

        if(content.length() <= maxLength) {
            return content;
        }

        if(keyword.length() >= maxLength) {
            return keyword.substring(0, maxLength);
        }


        int idx = content.indexOf(keyword);
        int mid = (maxLength - keyword.length()) /2;
        int gap = (maxLength - keyword.length()) %2 == 1 ? 1 : 0;

        int start = Math.max(0, idx - mid);
        int end = Math.min(content.length(), idx + keyword.length() + mid);


        if(start == 0) {
            end += -(idx - mid - gap);
        }else if(end == content.length()) {
            start -= (idx + keyword.length() + mid) - content.length() + gap;
        }

        return StringUtils.normalizeSpace(content.substring(start, end));
    }
}
