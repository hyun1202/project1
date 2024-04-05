package com.hyun.jobty.blog;

import com.hyun.jobty.domain.blog.dto.PostRes;
import com.hyun.jobty.domain.blog.service.BlogService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:application-dev.yaml")
@SpringBootTest(properties = "spring.profiles.active:dev")
public class BlogServiceTest {
    @Autowired
    BlogService blogService;

    @DisplayName("현재 게시글의 이전, 다음 게시글을 조회한다.")
    @Test
    void readPostPrevNextTest(){
        //given
        String domain = "0157942";
        int menu_seq = 1;
        int post_seq = 2;
        //when
        PostRes.PrevNextRes prevNext = blogService.findPrevNextPost(domain, menu_seq, post_seq);
        //then
        Assertions.assertEquals(prevNext.getPre().getPost_seq(), 1);
        Assertions.assertEquals(prevNext.getNext().getPost_seq(), 3);
    }
}
