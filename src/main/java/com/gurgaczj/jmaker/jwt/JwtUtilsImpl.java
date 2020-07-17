package com.gurgaczj.jmaker.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtilsImpl extends AbstractJwtUtils {

    public JwtUtilsImpl(@Value("${spring.application.name}") String appName,
                        @Value("${jmaker.jwt.secret}") String jwtSecret,
                        @Value("${jmaker.jwt.duration}") Integer duration) {
        super(appName, Keys.hmacShaKeyFor(jwtSecret.getBytes()), duration);
    }

}
