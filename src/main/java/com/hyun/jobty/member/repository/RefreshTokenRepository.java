package com.hyun.jobty.member.repository;

import com.hyun.jobty.member.domain.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<Token, String> {
    Optional<Token> findByRefreshToken(String refreshToken);
    Optional<Token> findByMemberId(String memberId);
}
