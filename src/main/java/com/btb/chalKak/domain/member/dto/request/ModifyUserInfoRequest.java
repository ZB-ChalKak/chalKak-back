package com.btb.chalKak.domain.member.dto.request;

import com.btb.chalKak.domain.member.type.Gender;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ModifyUserInfoRequest {

    private String nickname;
    private Gender gender;
    private Double height;
    private Double weight;
    private List<Long> styleTags;
    private String profileImg;
}