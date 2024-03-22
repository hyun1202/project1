package com.hyun.jobty.domain.member.service;

import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.member.domain.TokenType;
import com.hyun.jobty.domain.member.dto.MemberDto;

public interface MemberService {
    Member findByMemberSeq(int seq);
    Member findByMemberId(String id);
    Member tokenCheckAndUpdatePassword(String id, String token, MemberDto.Change req);
    String tokenCheckAndAccountActivate(String token_id, String token);
    Member signin(MemberDto.LoginReq addMemberRequest);
    Member signup(MemberDto.AddMemberReq addMemberReq);
    String signout(String member_id);
    String withdraw(String member_id);
    boolean findDuplicateId(String id);
    Member checkAccountStatus(String id);
    MemberDto.FindRes findAccountId(String member_id);
    MemberDto.FindRes findAccountPwd(String member_id);
    MemberDto.Check checkDuplicateId(String member_id);
    void checkTokenAndAccountId(String member_id, TokenType type);
}
