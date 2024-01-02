package com.hyun.jobty.setting.repository;

import com.hyun.jobty.member.domain.Member;
import com.hyun.jobty.setting.domain.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {
    Optional<Setting> findByDomain(String domain);
    Optional<Setting> findByMemberSeq(int seq);
    boolean existsByMember(Member member);
}
