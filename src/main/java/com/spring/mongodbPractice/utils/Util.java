package com.spring.mongodbPractice.utils;

import com.spring.mongodbPractice.config.Constants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Util {

    public static String getToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXIPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, Constants.SECURITY_KEY)
                .compact();
        return token;
    }
}
