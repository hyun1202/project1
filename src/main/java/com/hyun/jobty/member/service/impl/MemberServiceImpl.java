package com.hyun.jobty.member.service.impl;

import com.hyun.jobty.global.exception.CustomException;
import com.hyun.jobty.global.exception.ErrorCode;
import com.hyun.jobty.member.domain.Member;
import com.hyun.jobty.member.domain.Role;
import com.hyun.jobty.member.dto.MemberDto;
import com.hyun.jobty.member.repository.MemberRepository;
import com.hyun.jobty.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Member findByMemberSeq(int seq) {
        return memberRepository.findById(seq).orElseThrow(() -> new CustomException(ErrorCode.UserNotFound));
    }

    @Override
    public Member findByMemberId(String id) {
        return memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.UserNotFound));
    }

    @Override
    public boolean findDuplicateId(String id){
        return memberRepository.existsById(id);
    }

    @Override
    public Member reissuePassword(String pw){
        return null;
    }

    @Override
    public Member login(MemberDto.LoginReq loginReq) {
        // id 체크 로직
        Member member = memberRepository.findById(loginReq.getId()).orElseThrow(() -> new CustomException(ErrorCode.UserNotFound));

        // pw 체크 로직
        if (!bCryptPasswordEncoder.matches(loginReq.getPwd(), member.getPwd())){
            throw new CustomException(ErrorCode.IncorrectPassword);
        }

        // 만료 및 탈퇴 여부 확인
        if (!member.isEnabled())
            throw new CustomException(ErrorCode.AccountDisabled);

        if (!member.isAccountNonExpired())
            throw new CustomException(ErrorCode.AccountExpired);

        return member;
    }

    @Override
    @Transactional
    public String save(MemberDto.AddMemberReq addMemberReq) {
        if (memberRepository.findById(addMemberReq.getId()).orElse(null) != null){
            throw new CustomException(ErrorCode.DuplicatedId);
        }
        Member member = Member.builder().id(addMemberReq.getId())
                .pwd(bCryptPasswordEncoder.encode(addMemberReq.getPwd()))
                .nickname(addMemberReq.getNickname())
                .last_login_dt(LocalDateTime.now())
                .roles(Role.USER.getValue())
                .build();
        return memberRepository.save(member).getId();
    }

    @Override
    public String logout(String id) {
        return null;
    }

    @Override
    @Transactional
    public String withdraw(String id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.UserNotFound));
        member.setWithdraw_dt(LocalDateTime.now());
        return member.getId();
    }


}
