package com.btb.chalKak.domain.member.dto.response;

import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.member.type.Gender;
import com.btb.chalKak.domain.styleTag.entity.StyleTag;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class UserInfoResponse {

    private Long userId;
    private String nickname;
    private Gender gender;
    private Double height;
    private Double weight;
    private List<Long> styleTags;
    private String profileImg;

    public static UserInfoResponse fromEntity(Member member){
        return UserInfoResponse.builder()
                .userId(member.getId())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .height(member.getHeight())
                .weight(member.getWeight())
                .styleTags(member.getStyleTags().stream().map(StyleTag :: getId).collect(Collectors.toList()))
                .profileImg(member.getProfileImg())
                .build();
    }
}
