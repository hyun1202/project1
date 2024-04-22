package com.hyun.jobty.global.security.jwt;

import com.hyun.jobty.conf.security.jwt.JwtProperties;
import com.hyun.jobty.conf.security.jwt.TokenProvider;
import com.hyun.jobty.domain.member.domain.Member;
import com.hyun.jobty.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@TestPropertySource(locations = "classpath:application-dev.yaml")
@SpringBootTest(properties = "spring.profiles.active:dev")
public class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    @DisplayName("generateToken(): 유저 정보와 만료 기간 전달 및 토큰 생성")
    @Test
    void generateToken(){
        // given
        Member testMember = memberRepository.save(Member.builder()
                .email("test123")
                .pwd(bCryptPasswordEncoder.encode("test123"))
                .last_login_dt(LocalDateTime.now())
                .build());
        // when
        String token = tokenProvider.generateToken(testMember, Duration.ofDays(14));
        // then
        String id = tokenProvider.getMemberEmail(token);
        Assertions.assertThat(id).isEqualTo(testMember.getEmail());
    }

    @DisplayName("validToken(): 만료된 토큰일 때 유효성 검증 실패")
    @Test
    void validToken_invalidToken(){
        Date expiration = new Date(new Date().getTime() - Duration.ofDays(7).toMillis());

        // given
        String token = JwtFactory.builder()
                .expiration(expiration)
                .build()
                .createToken(jwtProperties);
        // when
        boolean result = tokenProvider.validToken(token);
        // then
        Assertions.assertThat(result).isFalse();
    }

    @DisplayName("validToken(): 유효한 토큰인 때 유효성 검증 성공")
    @Test
    void validToken_validToken(){
        // given
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);
        // when
        boolean result = tokenProvider.validToken(token);
        // then
        Assertions.assertThat(result).isTrue();
    }

    @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보 가져오기")
    @Test
    void getAuthentication(){
        // given
        String userId = "test123";
        String token = JwtFactory.builder()
                .subject(userId)
                .claims_body(userId)
                .build().createToken(jwtProperties);
        // when
        Authentication authentication = tokenProvider.getAuthentication(token);
        // then
        Assertions.assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userId);
    }

    @DisplayName("getEmail(): 토큰으로 유저 ID 가져오기")
    @Test
    void getMemberId(){
        // given
        String userId = "test123";
        String token = JwtFactory.builder()
                .claims_body(userId)
                .build()
                .createToken(jwtProperties);
        // when
        String userIdByToken = tokenProvider.getMemberEmail(token);
        // then
        Assertions.assertThat(userIdByToken).isEqualTo(userId);
    }

}
