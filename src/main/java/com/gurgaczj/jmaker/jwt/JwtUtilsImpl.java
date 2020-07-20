package com.gurgaczj.jmaker.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtilsImpl extends AbstractJwtUtils {

    public JwtUtilsImpl(@Value("${spring.application.name}") String appName,
                        @Value("${jmaker.jwt.secret}") String jwtSecret,
                        @Value("${jmaker.jwt.duration}") Integer duration) {
        super(appName, Keys.hmacShaKeyFor(jwtSecret.getBytes()), duration);
    }

}
