package com.hyun.jobty.member.service;

import com.hyun.jobty.member.domain.Token;

public interface TokenService {
    /**
     * 리프레시토큰에 해당하는 유저를 찾아 새 토큰 발급
     * @param memberId 토큰에 있는 사용자 아이디
     * @param refreshToken 리프레시 토큰
     * @return 새로 발급된 토큰 정보
     */
    Token reissueAccessToken(String refreshToken);

    /**
     * 새로운 토큰 발급
     * @param memberId 토큰에 있는 사용자 아이디
     * @return 새로 발급된 토큰 정보
     */
    Token createAccessToken(String memberId);

    boolean validAccessToken(String memberId, String accessToken);
}
