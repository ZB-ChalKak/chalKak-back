package com.btb.chalKak.domain.member.service.Impl;

import com.btb.chalKak.domain.member.dto.request.SignUpMemberRequest;
import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import com.btb.chalKak.domain.member.service.MemberService;
import com.btb.chalKak.domain.member.type.Gender;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import com.btb.chalKak.domain.styleTag.repository.StyleTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final StyleTagRepository styleTagRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Member saveMember(SignUpMemberRequest request) {
        // 1. 스타일 태그 조회
        List<String> keywords = request.getKeywords();

        // 따로 선택된 styletag의 members에서 안 엮어줘도?
        List<StyleTag> styleTags = new ArrayList<>();
        for(String keyword : keywords){
            styleTags.add(getStyleTagByKeyword(keyword));
        }

        // 2. 멤버 저장
        // 코드 컨벤션?
        return memberRepository.save(
                Member.builder()
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .nickname(request.getNickname())
                        .gender(Gender.valueOf(request.getGender()))
                        .height(request.getHeight())
                        .weight(request.getWeight())
                        .styleTags(styleTags)
                        .build()
        );
    }

    public StyleTag getStyleTagByKeyword(String keyword){
        return styleTagRepository.findByKeyword(keyword)
                .orElseThrow(() -> new RuntimeException("CustomMemberException"));
    }
}
