package com.hyun.jobty.domain.member.service.impl;

import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component("userDetailsService")
public class MemberDetailServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return memberRepository.findByUserId(id)
                .map(this::checkAcccount)
                .orElseThrow(() -> new CustomException(ErrorCode.UserNotFound));
    }

    /**
     * 계정 정보 확인 (만료, 탈퇴 여부 등)
     * @param member {@link Member}
     * @return 로그인 계정
     * @exception CustomException {@link ErrorCode}
     */
    private UserDetails checkAcccount(Member member){
        if (!checkAccountNonExpired(member))
            throw new CustomException(ErrorCode.AccountExpired);
        if (!checkAccountIsEnabled(member))
            throw new CustomException(ErrorCode.AccountDisabled);
        return member;
    }

    /**
     * 계정이 만료되었는지 확인
     * @param member {@link Member} 로그인 계정
     * @return 만료 시 true 아니면 false
     */
    private boolean checkAccountNonExpired(Member member){
        if (!member.isAccountNonExpired()) {
            // 계정 만료 시 실행 로직 작성
            return false;
        }
        return true;
    }

    /**
     * 계정 탈퇴 여부 확인
     * @param member {@link Member} 로그인 계정
     * @return 탈퇴 시 true 아니면 false
     */
    private boolean checkAccountIsEnabled(Member member){
        if (!member.isEnabled())
            return false;
        return true;
    }
}
