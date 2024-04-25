package com.hyun.jobty.domain.member.service;

import com.hyun.jobty.conf.security.jwt.TokenProvider;
import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.member.domain.Token;
import com.hyun.jobty.domain.member.domain.TokenType;
import com.hyun.jobty.domain.member.dto.TokenDto;
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
    public Token createJwtToken(String token_id, TokenDto dto){
        Member member = Member.builder()
                .uid(dto.getUid())
                .build();
        String accessToken = getAccessToken(member);
        String refreshToken = getRefreshToken(member);
        return Token.builder()
                .id(token_id)
                .uid(dto.getUid())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .exp(TokenType.signin.getExp())
                .build();
    }

    /**
     * jwt토큰을 업데이트(재발급)한다.
     * @param token 토큰 정보
     */
    public void reissueJwtToken(Token token){
        Member member = Member.builder()
                .uid(token.getUid())
                .build();
        String accessToken = getAccessToken(member);
        String refreshToken = getRefreshToken(member);
        token.updateToken(accessToken, refreshToken, TokenType.signin.getExp());
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
     * @param token 토큰
     * @return JWT 토큰 검증 여부
     */

    public boolean validJwtToken(String token){
        if (!tokenProvider.validToken(token)){
            return false;
        }
        return true;
    }

    /**
     * 만료된 access token을 위한 토큰 검증
     * 재발급을 위해 검사하는 access 토큰은 만료된 토큰은 정상이다.
     * @param accessToken 토큰
     * @return JWT 토큰 검증 여부
     */

    public boolean validPreAccessToken(String accessToken){
        if (!tokenProvider.validPreAccessToken(accessToken)){
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
