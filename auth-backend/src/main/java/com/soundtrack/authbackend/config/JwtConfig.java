package com.soundtrack.authbackend.config;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.oauth2.sdk.auth.Secret;

@Configuration
public class JwtConfig {
    /*
    @Value("${security.jwt.secret-key}")
    public String secreteKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("${security.jwt.algorithm}")
    private String algorithm; 

    public SecretKey getSecretKey() {
        var key = new OctetSequenceKey.Builder(secreteKey.getBytes())
                .algorithm(new JWSAlgorithm(algorithm))
                .build();
        return key.toSecretKey();
    }


    public JWSAlgorithm geAlgorithm() {
        return new JWSAlgorithm(algorithm);
    } */
}
