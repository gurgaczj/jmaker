package com.gurgaczj.jmaker.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTests {

    private String secret = "4u7x!A%D*G-KaPdSgVkXp2s5v8y/B?E(";
    private String appName = "someAppNane";
    private int jwtDuration = 24;

    @Test
    public void generateTokenTest() {
        try {
            String username = "username";
            String roleName = "SOME_ROLE";
            Collection<? extends GrantedAuthority> roles
                    = Collections.singletonList(new SimpleGrantedAuthority(roleName));
            String token = getJwtUtils().generateToken(username, roles);

            assertNotNull(token);
        } catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void parseTokenTest() {
        try {
            JwtUtils jwtUtils = getJwtUtils();

            String username = "username";
            Collection<? extends GrantedAuthority> roles
                    = Arrays.asList(new SimpleGrantedAuthority("SOME_ROLE"), new SimpleGrantedAuthority("SOME_ROLE2"));
            String token = jwtUtils.generateToken(username, roles);

            Jws<Claims> claimsJws = jwtUtils.parseClaims(token);
            assertNotNull(jwtUtils);

            Date expiartionDate = jwtUtils.getExpirationDate(token);
            assertTrue(expiartionDate.after(new Date()));

            String issuer = jwtUtils.getIssuer(token);
            assertEquals(issuer, username);

            Collection<? extends GrantedAuthority> authorities = jwtUtils.getGrantedAuthorities(token);

            authorities.forEach(grantedAuthority -> {
                assertTrue(roles.stream().anyMatch(role ->
                        grantedAuthority.getAuthority().equals(role.getAuthority())));
            });
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public JwtUtils getJwtUtils() throws NoSuchFieldException {
        return new JwtUtilsImpl(appName, secret, jwtDuration);
    }

}
