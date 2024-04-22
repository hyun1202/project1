package com.hyun.jobty.domain.member.service;

import com.hyun.jobty.conf.security.jwt.TokenProvider;
import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.member.domain.Token;
import com.hyun.jobty.domain.member.domain.TokenType;
import com.hyun.jobty.domain.member.dto.MemberDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class JwtTokenService {
    private final TokenProvider tokenProvider;

    /**
     * jwt토큰을 생성한다.
     * @param token_id  암호화된 토큰 아이디
     * @param dto 유저정보
     * @return 유저정보를 포함한 토큰값 리턴
     */
    public Token createJwtToken(String token_id, MemberDto dto){
        Member member = Member.builder()
                .uid(dto.getUid())
                .email(dto.getEmail())
                .build();
        String accessToken = getAccessToken(member);
        String refreshToken = getRefreshToken(member);
        return Token.builder()
                .id(token_id)
                .uid(dto.getUid())
                .email(dto.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .exp(TokenType.login.getExp() * 1000L)
                .build();
    }

    /**
     * 토큰을 생성한다
     * @param member 회원 정보
     * @return 생성한 토큰 문자열
     */
    private String getAccessToken(Member member){
        return tokenProvider.generateToken(member);
    }

    /**
     * 리프레시 토큰을 생성한다
     * @param member 회원 정보
     * @return 생성한 리프레시 토큰 문자열
     */
    private String getRefreshToken(Member member){
        return tokenProvider.generateRefreshToken(member);
    }

    /**
     * JWT 토큰 검증
     * @param accessToken 토큰
     * @return JWT 토큰 검증 여부
     */

    public boolean validJwtToken(String accessToken){
        if (!tokenProvider.validToken(accessToken)){
            return false;
        }
        return true;
    }

    /**
     * 토큰의 이메일을 반환
     * @param token jwt 토큰
     * @return 이메일 문자열
     */
    public String getTokenEmail(String token){
        return tokenProvider.getMemberEmail(token);
    }

    /**
     * 토큰의 UID를 반환
     * @param token jwt 토큰
     * @return UID 문자열
     */
    public String getTokenUid(String token){
        return tokenProvider.getMemberUid(token);
    }
}
