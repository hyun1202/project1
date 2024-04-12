package com.hyun.jobty.blog;

import com.hyun.jobty.domain.blog.domain.View;
import com.hyun.jobty.domain.blog.dto.LikeDto;
import com.hyun.jobty.domain.blog.dto.PostDto;
import com.hyun.jobty.domain.blog.repository.ViewRepository;
import com.hyun.jobty.domain.blog.service.BlogService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

@TestPropertySource(locations = "classpath:application-dev.yaml")
@SpringBootTest(properties = "spring.profiles.active:dev")
public class BlogServiceTest {
    @Autowired
    BlogService blogService;
    @Autowired
    ViewRepository viewRepository;

    @DisplayName("현재 게시글의 이전, 다음 게시글을 조회한다.")
    @Test
    void readPostPrevNextTest(){
        //given
        String domain = "0157942";
        int menu_seq = 1;
        Long post_seq = 2L;
        //when
        PostDto.PrevNextDto prevNext = blogService.findPrevNextPost(domain, menu_seq, post_seq);
        //then
        Assertions.assertEquals(prevNext.getPre().getPost_seq(), 1);
        Assertions.assertEquals(prevNext.getNext().getPost_seq(), 3);
    }

    @DisplayName("현재 게시글 조회수를 늘린다.")
    @Test
    void saveView(){
        //given
        View view = View.builder()
                .postSeq(1L)
                .date(LocalDate.now())
                .ip("127.0.0.2")
                .build();
        //when
        View saveView = viewRepository.save(view);
        //then
        Assertions.assertEquals(saveView.getDate().toString(), "2024-04-12");
    }

    @DisplayName("현재 게시글 조회수를 확인한다.")
    @Test
    void countView(){
        //given
        Long post_seq = 3L;
        LocalDate date = LocalDate.now().plusDays(1L);
        String ip = "127.0.0.3";
        //when
        Long count = viewRepository.countByPostSeqAndDate(post_seq, date);
        boolean isView = viewRepository.existsByPostSeqAndDateAndIp(post_seq, date, ip);
        //then
        Assertions.assertEquals(count, 1);
        Assertions.assertEquals(isView, true);
    }
    @DisplayName("현재 게시글 좋아요를 저장한다.")
    @Test
    void postLike(){
        //given
        Long post_seq = 2L;
        String id = "apfhd02579@naver.com";
        LikeDto likeDto = new LikeDto();
        likeDto.setId(id);
        //when
        blogService.postLikeSaveOrDelete(post_seq, likeDto);
        //then
    }
}
