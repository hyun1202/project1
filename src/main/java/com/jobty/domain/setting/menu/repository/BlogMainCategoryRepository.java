package com.jobty.domain.setting.menu.repository;

import com.jobty.domain.setting.menu.domain.BlogMainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogMainCategoryRepository extends JpaRepository<BlogMainCategory, Integer> {
    Optional<BlogMainCategory> findBySeq(int seq);
}
