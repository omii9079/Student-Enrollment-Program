package com.ediest.programenrollment.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

//Config classes are  made to tell logic-based configurations  // we need it because springboot is highlyCutomizable  it does not know everything about your project - so we teach it how to behave through config classes
// Jwt class tells how to set behaviour for it but here we have create beans manually for it
@Component
@Slf4j
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private Long expirationMs;

    public String generateToken(String subject, Map<String, Object> Claims) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder().
                setClaims(Claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                //.signwith() attach this secret to sign the token using HMAC-SHA algorithm.
                //secret.getBytes() Converts your secret key string(from application.yml)into bytes
                //keys.hmacShakeyfor() prepares a cryptographic key .
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))
                // compact() converts all your data (header, payload, signature ) into single compact String
                .compact();
    }

    public Jws<Claims> Validate(String token) {

        return Jwts.parserBuilder()// it gives you parser object which would verify that
                //JWT parser : This builder knows how to build a JwtParser — the class that can read, verify, and extract data from a JWT token.
                .setSigningKey(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))//like settings behaviour of parser object to use this key to verify it token
                .build()//actual parser object is created
                .parseClaimsJws(token);// parse the token with in three parts then it would decode everything it would like headers, payload , signature and recalucaltes  using secret key and compares both key if not matched then throw the error

    }

}
