package com.hyun.jobty.conf.security.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
    private String issuer;
    private String secretKey;

    public SecretKey getSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(this.secretKey.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }
}
