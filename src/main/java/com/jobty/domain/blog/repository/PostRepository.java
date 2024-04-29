package com.jobty.domain.blog.repository;

import com.jobty.domain.blog.domain.Post;
import com.jobty.domain.blog.domain.PrevNextInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * 도메인에 해당하는 게시글 조회
     * @param post_seq 게시글 번호
     * @param domain 도메인
     */
    Optional<Post> findBySetting_domainAndSeq(String domain, Long post_seq);

    /**
     * 페이징 처리한 게시글 조회
     * @param page 페이지
     * @param domain 도메인
     * @param menu_seq 메뉴번호
     */
    Page<Post> findAllBySetting_domainAndMenu_seq(Pageable page, String domain, Long menu_seq);

    @Query(value = "select a.prev_seq, a.next_seq " +
                " ,(select title from post where post_seq = a.prev_seq) prev_title " +
                " ,(select title from post where post_seq = a.next_seq) next_title " +
                "from (select `domain`, menu_seq, post_seq, " +
                "              LAG(post_seq, 1)  over (partition by `domain` order by post_seq) prev_seq," +
                "              LEAD(post_seq, 1)  over (partition by `domain` order by post_seq) next_seq " +
                "       from post " +
                "     ) a " +
                "where `domain` = :domain " +
                "  and menu_seq = :menu_seq " +
                "  and post_seq = :post_seq "
            , nativeQuery = true)
    PrevNextInterface findPrevNextPost(@Param("domain") String domain, @Param("menu_seq") Long menu_seq, @Param("post_seq") Long post_seq);
}
