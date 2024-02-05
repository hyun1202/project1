package com.hyun.jobty.member.service;

import com.hyun.jobty.member.domain.ConfirmToken;

public interface ConfirmTokenService {
    ConfirmToken findByToken(String token);
    String createToken(String memberId, int id);
    String createToken(String memberId);
    void deleteToken(String token);
}
