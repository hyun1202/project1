package com.jobty.domain.blog.repository;

import com.jobty.domain.blog.domain.View;
import com.jobty.domain.blog.domain.id.ViewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ViewRepository extends JpaRepository<View, ViewId> {
    Long countByPostSeqAndDate(Long post_seq, LocalDate date);
    boolean existsByPostSeqAndDateAndIp(Long post_seq, LocalDate date, String ip);
}
