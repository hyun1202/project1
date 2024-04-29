package com.jobty.domain.setting.detail.repository;

import com.jobty.domain.setting.detail.domain.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {
    Optional<Setting> findByDomain(String domain);
    Optional<Setting> findByMember_Uid(String uid);
    boolean existsByMember_Uid(String uid);
    boolean existsByDomain(String domain);
}
