package com.btb.chalKak.domain.member.dto.request;

import com.btb.chalKak.domain.member.type.Gender;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
public class ModifyUserInfoRequest {

    @NotNull
    private String nickname;

    @NotNull
    private Gender gender;

    @NotNull
    private Double height;

    @NotNull
    private Double weight;

    @NotNull
    private List<Long> styleTags;

    @NotNull
    private String profileImg;
}