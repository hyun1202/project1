package com.hyun.jobty.member.service.impl;

import com.hyun.jobty.global.exception.CustomException;
import com.hyun.jobty.global.exception.ErrorCode;
import com.hyun.jobty.member.domain.Member;
import com.hyun.jobty.member.repository.MemberRepository;
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
        System.out.println("loadUserByUsername 시작");
        return memberRepository.findById(id)
                .map(this::checkAccountNonExpired)
                .orElseThrow(() -> new CustomException(ErrorCode.UserNotFound));
    }

    /**
     * 계정이 만료되었는지 확인
     * @param member {@link Member} 로그인 계정
     * @return 로그인 계정
     * @exception CustomException {@link ErrorCode}.AccountExpired
     */
    private UserDetails checkAccountNonExpired(Member member){
        if (!member.isAccountNonExpired()) {
            // 계정 만료 시 실행 로직 작성
            throw new CustomException(ErrorCode.AccountExpired);
        }
        return member;
    }

}
