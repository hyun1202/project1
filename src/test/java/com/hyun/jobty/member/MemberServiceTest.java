package com.hyun.jobty.member;

import com.hyun.jobty.JobtyApplicationTests;
import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.member.domain.TokenType;
import com.hyun.jobty.domain.member.dto.MemberDto;
import com.hyun.jobty.domain.member.dto.TokenDto;
import com.hyun.jobty.domain.member.repository.MemberRepository;
import com.hyun.jobty.domain.member.service.MemberService;
import com.hyun.jobty.domain.member.service.TokenService;
import com.hyun.jobty.global.dto.CheckDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public class MemberServiceTest extends JobtyApplicationTests {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;
    @Autowired
    TokenService tokenService;

    // 한번에 테스트 케이스를 확인하기 위해 미리 임시로 발급할 아이디 입력
    String uid = "c309a3f2-cb7d-49e4-9d35-8c4e892a75a6";
    String email = "test1@test.com";
    String pwd = "asdfghjkl1!";
    String nickname = "test1";

//    @DisplayName("멤버를 추가한다.")
//    @Test
//    @Transactional
//    void addMemberTest(){
//        //given
//        String id = "test1";
//        String pwd = "asdfghjkl1!";
//        String nickname = "test1";
//        MemberDto.AddMemberReq addMemberReq = new MemberDto.AddMemberReq(id, pwd, nickname);
//        //when
//        Member member = memberService.signup(addMemberReq);
//        //then
//        Assertions.assertEquals(id, member.getUsername());
//    }

    @DisplayName("멤버를 조회한다")
    @Test
    void readMemberTest(){
        Member member = memberRepository.findById(uid).orElseThrow(() -> new CustomException(ErrorCode.FAIL));
        Assertions.assertEquals(member.getEmail(), email);
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
        TokenDto preToken = tokenService.createToken(req.getEmail(), type);
        TokenDto token = tokenService.createToken(req.getEmail(), type);
        //then
        // 토큰 타입 및 일치 확인
        boolean token1 = tokenService.validToken(preToken.getEmail(), preToken.getAccessToken(), type);
        boolean token2 = tokenService.validToken(token.getEmail(), token.getAccessToken(), type);
        Assertions.assertEquals(token1, false);
        Assertions.assertEquals(token2, true);
    }

    // 로그인 및 비밀번호 변경 등 모든 테스트 케이스 작성하기!

    // 중복 아이디 확인
    @DisplayName("중복 아이디를 확인한다.")
    @Test
    void checkIdTest() {
        // given
        String email = this.email;
        String email2 = "test2@test.com";
        // when
        CheckDto checkDto = memberService.checkDuplicateId(email);
        CheckDto checkDto2 = memberService.checkDuplicateId(email);
        // then
        Assertions.assertEquals(checkDto.isDuplicate(), true);
        Assertions.assertEquals(checkDto2.isDuplicate(), false);
    }
    // 아이디 활성화
    @DisplayName("아이디를 회원가입 토큰을 이용해서 활성화한다.")
    @Test
    @Transactional
    void ActivateTempAccountTest() {
        // given
        // 먼저 회원가입 토큰 생성이 필요
        TokenDto tokenDto = tokenService.createToken(email, TokenType.signin);
        // when
        // 아이디 활성화
        String email = memberService.tokenCheckAndAccountActivate(tokenDto.getToken_id(), tokenDto.getAccessToken());
        // then
        Assertions.assertEquals(email, this.email);
    }
    // 로그인
    @DisplayName("로그인")
    @Test
    void signInTest(){
        MemberDto.LoginReq req = new MemberDto.LoginReq(email, pwd);
        Member member = memberService.signin(req);
        Assertions.assertEquals(member.getUid(), uid);
        Assertions.assertEquals(member.getNickname(), nickname);
    }
    // 아이디 찾기
    @DisplayName("아이디 찾기")
    @Test
    void findByEmailTest(){

    }
    // 비밀번호 변경 토큰 생성
    @DisplayName("비밀번호를 찾기 위해 비밀번호 변경 토큰을 리턴한다")
    @Test
    void CreateChangePwdTokenTest() {

    }
    // 계정 비밀번호 변경(비회원)
    @DisplayName("비밀번호 변경 토큰을 이용해서 비밀번호를 변경한다.")
    @Test
    void changePwdTest() {

    }
    // 토큰 재발급
    @DisplayName("만료된 access토큰을 refresh토큰을 이용하여 재발급 한다.")
    @Test
    void reissueAccessTokenTest() {

    }
    // 로그아웃
    @DisplayName("로그아웃")
    @Test
    void signOutTest(){

    }
    // 회원 탈퇴
    @DisplayName("회원탈퇴")
    @Test
    void withdrawTest() {

    }
}
