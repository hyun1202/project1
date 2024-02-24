package com.hyun.jobty.domain.blog.repository;

import com.hyun.jobty.domain.blog.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * 페이징 처리한 게시글 조회
     * @param page 페이지
     * @param domain 도메인
     * @param menu_seq 메뉴번호
     */
    Page<Post> findAllBySetting_domainAndMenu_seq(Pageable page, String domain, int menu_seq);
}
