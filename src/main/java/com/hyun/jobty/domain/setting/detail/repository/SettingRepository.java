package com.hyun.jobty.domain.setting.detail.repository;

import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.setting.detail.domain.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {
    Optional<Setting> findByDomain(String domain);
    Optional<Setting> findByMemberSeq(String seq);
    boolean existsByMember(Member member);
}
