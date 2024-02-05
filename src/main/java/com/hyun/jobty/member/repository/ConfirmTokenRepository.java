package com.hyun.jobty.member.repository;

import com.hyun.jobty.member.domain.ConfirmToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmTokenRepository extends CrudRepository<ConfirmToken, String> {
    Optional<ConfirmToken> findByToken(String token);

}
