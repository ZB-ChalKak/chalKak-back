package com.btb.chalKak.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SignUpMemberRequest {

    private String email;
    private String password;
    private String nickname;
    private String gender;
    private Double height;
    private Double weight;
    private List<String> keywords;
}
