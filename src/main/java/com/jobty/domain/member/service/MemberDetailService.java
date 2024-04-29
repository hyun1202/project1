package com.jobty.domain.member.service;

import com.jobty.advice.exception.CustomException;
import com.jobty.advice.exception.ErrorCode;
import com.jobty.domain.member.domain.Member;
import com.jobty.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return memberRepository.findByEmail(id)
                .map(this::checkAccount)
                .orElseThrow(() -> new CustomException(ErrorCode.UserNotFound));
    }

    /**
     * 계정 정보 확인 (탈퇴 여부 등)
     * @param member {@link Member}
     * @return 로그인 계정
     * @exception CustomException {@link ErrorCode} AccountDisabled
     */
    private UserDetails checkAccount(Member member){
        if (!checkAccountIsEnabled(member))
            throw new CustomException(ErrorCode.AccountDisabled);
        return member;
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
