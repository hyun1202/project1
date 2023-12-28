package com.hyun.jobty.member.repository;

import com.hyun.jobty.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findById(String id);
    Optional<Member> findByIdAndPwd(String id, String pwd);
    boolean existsById(String id);
}
