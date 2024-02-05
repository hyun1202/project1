package com.hyun.jobty.member.repository;

import com.hyun.jobty.member.domain.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<Token, String> {
    Optional<Token> findByRefreshToken(String refreshToken);
    Optional<Token> findByMemberId(String memberId);
}
