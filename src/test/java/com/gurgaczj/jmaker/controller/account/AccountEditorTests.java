package com.gurgaczj.jmaker.controller.account;

import com.gurgaczj.jmaker.dto.AccountDto;
import com.gurgaczj.jmaker.h2.TestDataInitializer;
import com.gurgaczj.jmaker.jwt.JwtUtils;
import com.gurgaczj.jmaker.model.NewPassword;
import com.gurgaczj.jmaker.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AccountEditorTests {

    @LocalServerPort
    private int port;

    private JwtUtils jwtUtils;

    private final String username = TestDataInitializer.TEST_USER;
    private final String password = TestDataInitializer.TEST_USER_PASS;

    @Test
    public void testEditPassword(){
        NewPassword newPassword = new NewPassword(password, "newPassword!1", "newPassword!1");

        TestDataInitializer.TEST_USER_PASS = newPassword.getNewPassword();

        String token = jwtUtils.generateToken(username, Arrays.asList(new SimpleGrantedAuthority(Role.getRole(1))));

        Mono<String> request = WebClient
                .create("http://localhost:" + port + "/api/account/password")
                .put()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(Mono.just(newPassword), NewPassword.class)
                .retrieve()
                .bodyToMono(String.class);

        StepVerifier.create(request)
                .assertNext(s -> Assertions.assertEquals("ok", s))
                .expectComplete()
                .verify();
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
}
