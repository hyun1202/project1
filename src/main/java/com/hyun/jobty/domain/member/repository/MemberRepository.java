package com.hyun.jobty.domain.member.repository;

import com.hyun.jobty.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findById(String id);
    boolean existsById(String id);
}
