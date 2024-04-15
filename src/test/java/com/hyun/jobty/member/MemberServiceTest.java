package com.hyun.jobty.member;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.member.dto.MemberDto;
import com.hyun.jobty.domain.member.repository.MemberRepository;
import com.hyun.jobty.domain.member.service.MemberService;
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

    @Autowired
    MemberService memberService;
    @DisplayName("멤버를 조회한다")
    @Test
    void test(){
        Member member = memberRepository.findById("58946fa8-b84b-466c-a5a0-3160891e6ba8").orElseThrow(() -> new CustomException(ErrorCode.FAIL));
        Assertions.assertEquals(member.getEmail(), "test1");
    }

    @DisplayName("멤버를 추가한다.")
    @Test
    void addMember(){
        //given
        //// AddMemberReq
        String id = "test1";
        String pwd = "asdfghjkl1!";
        String nickname = "test1";
        MemberDto.AddMemberReq addMemberReq = new MemberDto.AddMemberReq(id, pwd, nickname);
        //when
        Member member = memberService.signup(addMemberReq);
        //then
        Assertions.assertEquals(id, member.getUsername());
    }
}
