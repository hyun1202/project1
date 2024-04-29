package com.hyun.jobty.global.token;

import com.hyun.jobty.JobtyApplicationTests;
import com.hyun.jobty.domain.member.domain.Token;
import com.hyun.jobty.domain.member.repository.TokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TokenServiceTest extends JobtyApplicationTests {
    @Autowired
    TokenRepository tokenRepository;

    @DisplayName("토큰의 만료시간을 짧게 지정 후 모든 데이터가 삭제되는지 확인")
    @Test
    void createTokenAndCheckDataDeleted() throws InterruptedException {
        // given
        String uid = "aaaa";
        Token token = Token.builder()
                .uid(uid)
                .id("a_id")
                .accessToken("access")
                .refreshToken("refresh")
                .exp(10L)
                .build();
        // when
        Token savedToken = tokenRepository.save(token);
        // then
        Assertions.assertEquals(savedToken.getUid(), uid);
        Token testToken = tokenRepository.findById(uid).orElse(new Token("bbb", "asdf", "aa", "bb", 1L));
        Assertions.assertNotEquals(testToken.getUid(), uid);
        Thread.sleep(10000);
        testToken = tokenRepository.findById(uid).orElse(new Token("bbb", "asdf", "aa", "bb", 1L));
        Assertions.assertNotEquals(testToken.getUid(), uid);
    }
}
