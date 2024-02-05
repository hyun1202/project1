package com.hyun.jobty.member.service.impl;

import com.hyun.jobty.global.exception.CustomException;
import com.hyun.jobty.global.exception.ErrorCode;
import com.hyun.jobty.global.util.Util;
import com.hyun.jobty.member.domain.ConfirmToken;
import com.hyun.jobty.member.repository.ConfirmTokenRepository;
import com.hyun.jobty.member.service.ConfirmTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfirmTokenServiceImpl implements ConfirmTokenService {
    private final ConfirmTokenRepository confirmTokenRepository;
    @Override
    public ConfirmToken findByToken(String token) {
        return confirmTokenRepository.findByToken(token).orElseThrow(() -> new CustomException(ErrorCode.ConfirmTokenNotFound));
    }

    @Override
    public String createToken(String memberId, int seq) {
        ConfirmToken token = ConfirmToken.builder()
                .token(Util.random(1, 32))
                .userId(memberId)
                .seq(seq)
                .build();
        return confirmTokenRepository.save(token).getToken();
    }


    @Override
    public void deleteToken(String token) {
        confirmTokenRepository.delete(this.findByToken(token));
    }


}
