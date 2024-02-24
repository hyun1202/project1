package com.hyun.jobty.domain.member.repository;

import com.hyun.jobty.domain.member.domain.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
}
