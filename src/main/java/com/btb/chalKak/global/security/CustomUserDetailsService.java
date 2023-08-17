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
    public UserDetails loadUserByUsername(String userPK) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.parseLong(userPK))
                .orElseThrow(RuntimeException::new);    // TODO : CustomException 구현 + Exception 메서드 단위로 따로 빼기?

        return new CustomUserDetails(member);
    }
}
