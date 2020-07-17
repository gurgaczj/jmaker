package com.gurgaczj.jmaker.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;

public abstract class AbstractJwtUtils implements JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(AbstractJwtUtils.class);

    private final String appName;
    private final SecretKey secretKey;
    private final Integer duration;

    public AbstractJwtUtils(String appName, SecretKey secretKey, Integer duration) {
        this.appName = appName;
        this.secretKey = secretKey;
        this.duration = duration;
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities){
        logger.info("Generating token for " + username);
        return Jwts.builder()
                .setAudience(appName)
                .setIssuedAt(new Date())
                .setExpiration(getExpirationDate())
                .setIssuer(username)
                .signWith(secretKey)
                .claim("authorities", authorities)
                .compact();
    }

    public Jws<Claims> parseClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .requireAudience(appName)
                .build()
                .parseClaimsJws(token);
    }

    public Date getExpirationDate(String token) {
        return parseClaims(token).getBody().getExpiration();
    }

    public String getIssuer(String token) {
        return parseClaims(token).getBody().getIssuer();
    }

    public Collection<SimpleGrantedAuthority> getGrantedAuthorities(String token) {
        // This is the way xD
        @SuppressWarnings("unchecked")
        List<LinkedHashMap<String, String>> listMap =
                (List<LinkedHashMap<String, String>>) parseClaims(token).getBody().get("authorities");

        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();

        for(LinkedHashMap<String, String> map: listMap){
            for(Map.Entry<String, String> authEntry: map.entrySet()){
                String role = authEntry.getValue();
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }

        return authorities;
    }

    protected Date getExpirationDate(){
        return Date.from(Instant.now().plusSeconds(duration * 60 * 60));
    }
}
