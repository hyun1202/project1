package com.hyun.jobty.member.service.impl;

import com.hyun.jobty.global.exception.CustomException;
import com.hyun.jobty.global.exception.ErrorCode;
import com.hyun.jobty.member.domain.Member;
import com.hyun.jobty.member.domain.Role;
import com.hyun.jobty.member.dto.MemberDto;
import com.hyun.jobty.member.repository.MemberRepository;
import com.hyun.jobty.member.service.MemberService;
import com.hyun.jobty.member.service.TokenService;
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
    public Member login(MemberDto.LoginRequest loginRequest) {
        // id 체크 로직
        Member member = memberRepository.findById(loginRequest.getId()).orElseThrow(() -> new CustomException(ErrorCode.UserNotFound));
        // pw 체크 로직
        if (!bCryptPasswordEncoder.matches(loginRequest.getPwd(), member.getPwd())){
            throw new CustomException(ErrorCode.IncorrectPassword);
        }
        return member;
    }

    @Override
    @Transactional
    public String save(MemberDto.AddMemberRequest addMemberRequest) {
        if (memberRepository.findById(addMemberRequest.getId()).orElse(null) != null){
            throw new CustomException(ErrorCode.DuplicatedId);
        }
        Member member = Member.builder().id(addMemberRequest.getId())
                .pwd(bCryptPasswordEncoder.encode(addMemberRequest.getPwd()))
                .nickname(addMemberRequest.getNickname())
                .last_login_dt(LocalDateTime.now())
                .roles(Role.USER.getValue())
                .build();
        return memberRepository.save(member).getId();
    }


}
