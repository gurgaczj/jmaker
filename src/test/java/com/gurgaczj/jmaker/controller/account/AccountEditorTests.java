package com.gurgaczj.jmaker.controller.account;

import com.gurgaczj.jmaker.dto.AccountDto;
import com.gurgaczj.jmaker.h2.TestDataInitializer;
import com.gurgaczj.jmaker.jwt.JwtUtils;
import com.gurgaczj.jmaker.model.Email;
import com.gurgaczj.jmaker.model.NewPassword;
import com.gurgaczj.jmaker.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

/**
 * Place here tests that edits account.
 */
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

        TestDataInitializer.setTestUserPass(newPassword.getNewPassword());

        String token = jwtUtils.generateToken(username, Arrays.asList(new SimpleGrantedAuthority(Role.getRole(1))));

        Mono<ClientResponse> request = WebClient
                .create("http://localhost:" + port + "/api/account/password")
                .put()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(Mono.just(newPassword), NewPassword.class)
                .exchange();

        StepVerifier.create(request)
                .assertNext(clientResponse -> Assertions.assertEquals(HttpStatus.OK, clientResponse.statusCode()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testEditPassword_passwordsAreNotTheSame(){
        NewPassword newPassword = new NewPassword(password, "newPassword!1", "notTheSamePassword");

        String token = jwtUtils.generateToken(username, Arrays.asList(new SimpleGrantedAuthority(Role.getRole(1))));

        Mono<ClientResponse> request = WebClient
                .create("http://localhost:" + port + "/api/account/password")
                .put()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(Mono.just(newPassword), NewPassword.class)
                .exchange();

        StepVerifier.create(request)
                .assertNext(clientResponse -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, clientResponse.statusCode()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testEditEmail(){
        Email email = new Email("new.some@mail.com");

        String token = jwtUtils.generateToken(username, Arrays.asList(new SimpleGrantedAuthority(Role.getRole(1))));

        Mono<AccountDto> request = WebClient
                .create("http://localhost:" + port + "/api/account/email")
                .put()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(Mono.just(email), Email.class)
                .retrieve()
                .bodyToMono(AccountDto.class);

        StepVerifier.create(request)
                .assertNext(response -> Assertions.assertEquals(email.getEmail(), response.getEmail()))
                .expectComplete()
                .verify();
    }

    @Test
    public void testEditEmail_newEmailDoesNotMeetRequirements(){
        Email email = new Email("new.some@.com");

        String token = jwtUtils.generateToken(username, Arrays.asList(new SimpleGrantedAuthority(Role.getRole(1))));

        Mono<ClientResponse> request = WebClient
                .create("http://localhost:" + port + "/api/account/email")
                .put()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(Mono.just(email), Email.class)
                .exchange();

        StepVerifier.create(request)
                .assertNext(response -> Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode()))
                .expectComplete()
                .verify();
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
}
