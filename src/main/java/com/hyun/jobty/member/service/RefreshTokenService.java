package com.hyun.jobty.member.service;

import com.hyun.jobty.member.domain.Token;

public interface RefreshTokenService {
    Token findByMemberId(String memberId);
    Token findByRefreshToken(String refreshToken);
//    String updateRefreshToken(String memberId, String refreshToken);
    String saveAccessToken(String memberId, String accessToken);

    Token saveToken(String memberId, String accessToken, String refreshToken);

    String saveRefreshToken(String memberId, String refreshToken);

    void deleteToken(String memberId);
}
