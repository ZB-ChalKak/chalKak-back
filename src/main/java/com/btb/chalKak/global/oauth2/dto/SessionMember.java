package com.btb.chalKak.global.oauth2.dto;

import com.btb.chalKak.domain.member.entity.Member;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SessionMember implements Serializable {

  private String name;
  private String email;
  private String profileImg;

  public SessionMember(Member member) {
      this.name = member.getNickname();
      this.email = member.getEmail();
      this.profileImg = member.getProfileImg();
  }
}
