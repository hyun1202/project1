package com.hyun.jobty.domain.member.service;

import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.member.dto.MemberDto;

public interface MemberService {
    Member findByMemberSeq(int seq);
    Member findByMemberId(String id);
    Member tokenCheckAndUpdatePassword(String id, String token, MemberDto.Change req);
    String tokenCheckAndAccountActivate(String id, String token);
    Member signin(MemberDto.LoginReq addMemberRequest);
    Member signup(MemberDto.AddMemberReq addMemberReq);
    String signout(String member_id);
    String withdraw(String member_id);
    boolean findDuplicateId(String id);
    Member checkAccountStatus(String id);
}
