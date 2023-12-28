package com.hyun.jobty.member.service.impl;

import com.hyun.jobty.global.exception.CustomException;
import com.hyun.jobty.global.exception.ErrorCode;
import com.hyun.jobty.member.domain.Token;
import com.hyun.jobty.member.repository.RefreshTokenRepository;
import com.hyun.jobty.member.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Token findByMemberId(String memberId) {
        return refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.TokenUserNotFound));
    }

    @Override
    public Token findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.TokenExpired));
    }

    @Transactional
    public String updateRefreshToken(String memberId, String refreshToken){
        Token Token = refreshTokenRepository.findByMemberId(memberId).orElseThrow(() -> new CustomException(ErrorCode.UnexpectedToken));
        Token.setRefreshToken(refreshToken);
        return Token.getRefreshToken();
    }

    @Override
    public String saveAccessToken(String memberId, String accessToken){
        Token token = new Token();
        token.setMemberId(memberId);
        token.setAccessToken(accessToken);
        return refreshTokenRepository.save(token).getAccessToken();
    }

    @Override
    public Token saveToken(String memberId, String accessToken, String refreshToken){
        Token token = new Token();
        token.setMemberId(memberId);
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        return refreshTokenRepository.save(token);
    }

    @Override
    public String saveRefreshToken(String memberId, String refreshToken){
        Token token = new Token();
        token.setMemberId(memberId);
        token.setRefreshToken(refreshToken);
        return refreshTokenRepository.save(token).getRefreshToken();
    }

    @Override
    public void deleteToken(String memberId) {
        Token token = refreshTokenRepository.findByMemberId(memberId).orElseThrow(()->new CustomException(ErrorCode.TokenExpired));
        refreshTokenRepository.delete(token);
    }
}
