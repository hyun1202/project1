package com.hyun.jobty.domain.blog.repository;

import com.hyun.jobty.domain.blog.domain.Like;
import com.hyun.jobty.domain.blog.domain.id.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {
    boolean existsByPostSeqAndMemberUid(Long post_seq, String member_uid);
}
