package com.hyun.jobty.member.service.impl;

import com.hyun.jobty.global.exception.CustomException;
import com.hyun.jobty.global.exception.ErrorCode;
import com.hyun.jobty.member.domain.ConfirmToken;
import com.hyun.jobty.member.domain.Member;
import com.hyun.jobty.member.domain.Role;
import com.hyun.jobty.member.domain.Status;
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
    private final TokenService tokenService;
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
    @Transactional
    public Member tokenCheckAndUpdatePassword(String token, MemberDto.Change req){
        String id = tokenService.checkConfirmToken(token).getUserId();
        Member member = findByMemberId(id);
        member.updatePassword(bCryptPasswordEncoder.encode(req.getPwd()));
        tokenService.deleteConfirmToken(token);
        return member;
    }

    @Override
    @Transactional
    public String emailCheckAndAccountActivate(String token) {
        ConfirmToken confirmToken = tokenService.checkConfirmToken(token);
        Member member = this.findByMemberSeq(confirmToken.getSeq());
        member.memberActivate();
        // 토큰 확인 완료하였으므로 토큰 삭제
        tokenService.deleteConfirmToken(token);
        return member.getId();
    }

    @Override
    @Transactional
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

        member.setLast_login_dt(LocalDateTime.now());

        return member;
    }

    @Override
    @Transactional
    public Member save(MemberDto.AddMemberReq addMemberReq) {
        // 중복 아이디 확인
        if (this.findDuplicateId(addMemberReq.getId())){
            throw new CustomException(ErrorCode.DuplicatedId);
        }
        Member member = Member.builder().id(addMemberReq.getId())
                .pwd(bCryptPasswordEncoder.encode(addMemberReq.getPwd()))
                .nickname(addMemberReq.getNickname())
                .last_login_dt(LocalDateTime.now())
                .roles(Role.USER.getValue())
                // 이메일 확인 전까지 임시 계정 상태로 생성
                .status(Status.TEMPORARY.ordinal())
                .build();
        // 임시 계정 토큰 생성

        return memberRepository.save(member);
    }

    @Override
    public String logout(String id) {
        tokenService.deleteToken(id);
        return id;
    }

    @Override
    @Transactional
    public String withdraw(String id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.UserNotFound));
        member.memberWithdraw();
        return member.getId();
    }
}
