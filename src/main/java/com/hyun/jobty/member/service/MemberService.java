package com.hyun.jobty.member.service;

import com.hyun.jobty.member.domain.Member;
import com.hyun.jobty.member.dto.MemberDto;

import java.util.List;

public interface MemberService {
    List<Member> getAllMembers();
    Member findByMemberSeq(int seq);
    Member findByMemberId(String id);
    Member login(MemberDto.LoginReq addMemberRequest);
    Member save(MemberDto.AddMemberReq addMemberReq);
    String logout(String id);
    String withdraw(String id);
    boolean findDuplicateId(String id);
    Member tokenCheckAndUpdatePassword(String token, MemberDto.Change req);
    String emailCheckAndAccountActivate(String token);
}
