package com.btb.chalKak.domain.member.dto;

import com.btb.chalKak.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Writer {

    private Long id;
    private String nickname;
    private String profileImg;

    private double height;
    private double weight;

    public static Writer fromEntity(Member member) {
        return Writer.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImg(member.getProfileImg())
                .height(member.getHeight())
                .weight(member.getWeight())
                .build();
    }

}
