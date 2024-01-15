package com.hyun.jobty.setting.repository;

import com.hyun.jobty.setting.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Optional<Menu> findBySetting_Domain(String domain);
}
