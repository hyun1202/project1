package com.hyun.jobty.member;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


@TestPropertySource(locations = "classpath:application-dev.yaml")
@SpringBootTest(properties = "spring.profiles.active:dev")
public class MemberServiceTest {
    @Autowired
    MemberRepository memberRepository;

    //afca70d5-2999-4ec5-bea2-a1a04263a9d2
    @DisplayName("멤버를 조회한다")
    @Test
    void test(){
        Member member = memberRepository.findById("afca70d5-2999-4ec5-bea2-a1a04263a9d2").orElseThrow(() -> new CustomException(ErrorCode.FAIL));
        Assertions.assertEquals(member.getUserId(), "0157942@gmail.com");
    }
}
