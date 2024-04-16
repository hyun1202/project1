package com.hyun.jobty.member;

import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.member.domain.Token;
import com.hyun.jobty.domain.member.domain.TokenType;
import com.hyun.jobty.domain.member.dto.MemberDto;
import com.hyun.jobty.domain.member.repository.MemberRepository;
import com.hyun.jobty.domain.member.service.MemberService;
import com.hyun.jobty.domain.member.service.TokenService;
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
    @Autowired
    TokenService tokenService;

    @DisplayName("멤버를 추가한다.")
    @Test
    void addMemberTest(){
        //given
        String id = "test1";
        String pwd = "asdfghjkl1!";
        String nickname = "test1";
        MemberDto.AddMemberReq addMemberReq = new MemberDto.AddMemberReq(id, pwd, nickname);
        //when
        Member member = memberService.signup(addMemberReq);
        //then
        Assertions.assertEquals(id, member.getUsername());
    }
    @DisplayName("멤버를 조회한다")
    @Test
    void readMemberTest(){
        Member member = memberRepository.findById("58946fa8-b84b-466c-a5a0-3160891e6ba8").orElseThrow(() -> new CustomException(ErrorCode.FAIL));
        Assertions.assertEquals(member.getEmail(), "test1");
    }

    @DisplayName("임시계정 활성화를 위한 토큰 생성 후 토큰 재생성 시 이전 토큰 폐기 확인")
    @Test
    void createTempAccountTokenAndMailResendTest(){
        //given
        String email = "apfhd0257@naver.com";
        TokenType type = TokenType.signup;

        MemberDto.FindReq req = new MemberDto.FindReq(email);
        //when
        // 토큰 생성 후 토큰 재발급
        Token preToken = tokenService.createToken(req.getId(), type);
        Token token = tokenService.createToken(req.getId(), type);
        //then
        // 토큰 타입 및 일치 확인
        boolean token1 = tokenService.validToken(preToken.getId(), preToken.getAccessToken(), type);
        boolean token2 = tokenService.validToken(token.getId(), token.getAccessToken(), type);
        Assertions.assertEquals(token1, false);
        Assertions.assertEquals(token2, true);
    }
}
