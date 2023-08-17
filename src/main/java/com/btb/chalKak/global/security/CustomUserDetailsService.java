package com.btb.chalKak.global.security;

import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = getMember(email);

        return new CustomUserDetails(member);
    }

    private Member getMember(String email) {
        Member member = memberRepository.findById(Long.parseLong(email))
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보를 불러오는데 실패했습니다."));

        return member;
    }
}
