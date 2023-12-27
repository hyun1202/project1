package com.hyun.jobty.global.security.jwt;

import com.hyun.jobty.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Service
public class TokenProvider{
    private static final String AUTHORITIES_KEY = "auth";
    private final JwtProperties jwtProperties;
    private final String CLAIM_ID = "id";
    private final Duration TOKEN_EXPIRED_AT = Duration.ofHours(2);
    private final Duration REFRESH_EXPIRED_AT = Duration.ofHours(12);

    public String generateToken(Member member, Duration expiredAt){
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), member);
    }

    public String generateToken(Member member){
        Date now = new Date();
        return makeToken(new Date(now.getTime() + TOKEN_EXPIRED_AT.toMillis()), member);
    }

    public String generateRefreshToken(Member member){
        Date now = new Date();
        return makeToken(new Date(now.getTime() + REFRESH_EXPIRED_AT.toMillis()), member);
    }

    public String makeToken(Date expiry, Member member){
        Date now = new Date();

        return Jwts.builder()
                .header().type("JWT").and()
                .claims()
                    .add(CLAIM_ID, member.getId())
                .add(AUTHORITIES_KEY, "ROLE_USER")
                    .subject(member.getId())
                    .issuedAt(now)
                    .expiration(expiry)
                    .issuer(jwtProperties.getIssuer())
                .and()
                .signWith(jwtProperties.getSecretKey(), Jwts.SIG.HS512)
                .compact();
    }

    // 토큰 검증
    public boolean validToken(String token){
        try{
            Jwts.parser().verifyWith(jwtProperties.getSecretKey()).build().parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public Authentication getAuthentication(String token){
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new User(claims.getSubject(), "", authorities), token, authorities);
    }

    /**
     * 토큰에 담겨있는 MemberID 조회
     * @param token 토큰 정보
     * @return memberID
     */
    public String getMemberId(String token){
        Claims claims = getClaims(token);
        return claims.get(CLAIM_ID, String.class);
    }

    // 클레임 조회
    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(jwtProperties.getSecretKey())   // 키 지정
                .build()
                .parseSignedClaims(token).getPayload();
    }
}


