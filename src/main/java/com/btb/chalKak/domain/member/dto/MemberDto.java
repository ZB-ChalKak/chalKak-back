package com.btb.chalKak.domain.member.dto;

import com.btb.chalKak.domain.member.type.Gender;
import com.btb.chalKak.domain.member.type.MemberRole;
import com.btb.chalKak.domain.member.type.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String email;
    private String nickname;
    private String password;
    private String profileImg;

    private double height;
    private double weight;

    private boolean privacyHeight;
    private boolean privacyWeight;

    private Gender gender;
    private MemberStatus status;
    private MemberRole role;

}
