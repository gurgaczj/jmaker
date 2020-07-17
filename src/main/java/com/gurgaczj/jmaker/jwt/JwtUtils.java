package com.gurgaczj.jmaker.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

public interface JwtUtils {

    String generateToken(String username, Collection<? extends GrantedAuthority> authorities);

    Jws<Claims> parseClaims(String token);

    Date getExpirationDate(String token);

    String getIssuer(String token);

    Collection<SimpleGrantedAuthority> getGrantedAuthorities(String token);

}
