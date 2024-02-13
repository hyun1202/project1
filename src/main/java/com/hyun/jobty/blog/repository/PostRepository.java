package com.hyun.jobty.blog.repository;

import com.hyun.jobty.blog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    /**
     * 도메인에 해당하는 게시글 조회
     * @param seq 게시글 번호
     * @param domain 도메인
     */
    Optional<Post> findBySeqAndSetting_domain(int seq, String domain);
}
