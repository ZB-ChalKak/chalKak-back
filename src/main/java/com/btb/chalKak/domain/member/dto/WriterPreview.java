package com.btb.chalKak.domain.member.dto;

import com.btb.chalKak.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WriterPreview {

    private Long id;
    private String nickname;
    private String profileImg;

    public static WriterPreview fromEntity(Member member) {
        return WriterPreview.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImg(member.getProfileImg())
                .build();
    }

}
