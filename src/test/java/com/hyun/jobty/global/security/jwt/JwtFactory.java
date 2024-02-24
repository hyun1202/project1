package com.hyun.jobty.global.security.jwt;

import com.hyun.jobty.conf.security.jwt.JwtProperties;
import io.jsonwebtoken.Jwts;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Getter
public class JwtFactory {
    private String subject = "test@test.com";
    private Date issuedAt = new Date();
    private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
    private Map<String, Object> claims = Collections.emptyMap();

    @Builder
    public JwtFactory(String subject, Date issuedAt, Date expiration, String claims_body){
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiration = expiration != null ? expiration : this.expiration;
        this.claims = claims_body != null ? Map.of("id", claims_body) : this.claims;
    }

    public static JwtFactory withDefaultValues(){
        return JwtFactory.builder().build();
    }

    public String createToken(JwtProperties jwtProperties){
        return Jwts.builder()
                .header().type("JWT").and()
                .claims()
                    .add(claims)
                    .subject(subject)
                    .issuedAt(issuedAt)
                    .expiration(expiration)
                .and()
                .signWith(jwtProperties.getSecretKey(), Jwts.SIG.HS512)
                .compact();
    }
}
