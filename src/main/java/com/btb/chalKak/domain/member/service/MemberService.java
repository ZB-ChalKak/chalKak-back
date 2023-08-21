package com.btb.chalKak.domain.member.service;

import com.btb.chalKak.domain.member.dto.request.SignUpMemberRequest;
import com.btb.chalKak.domain.member.entity.Member;

public interface MemberService {

    Member saveMember(SignUpMemberRequest request);
}
