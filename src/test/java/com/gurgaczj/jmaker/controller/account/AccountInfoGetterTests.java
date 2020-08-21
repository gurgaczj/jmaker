package com.gurgaczj.jmaker.controller.account;

import com.gurgaczj.jmaker.dto.AccountDto;
import com.gurgaczj.jmaker.dto.AccountLessInfoDto;
import com.gurgaczj.jmaker.h2.TestDataInitializer;
import com.gurgaczj.jmaker.jwt.JwtUtils;
import com.gurgaczj.jmaker.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AccountInfoGetterTests {

    @LocalServerPort
    private int port;

    private final String username = TestDataInitializer.TEST_USER;
    private final String password = TestDataInitializer.TEST_USER_PASS;

    private JwtUtils jwtUtils;

    @Test
    public void getAccountTest() {
        String token = jwtUtils.generateToken(username, Arrays.asList(new SimpleGrantedAuthority(Role.getRole(1))));

        Mono<AccountDto> request = WebClient
                .create("http://localhost:" + port + "/api/account")
                .get()
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(AccountDto.class);

        StepVerifier
                .create(request)
                .assertNext(accountDto -> assertEquals(username, accountDto.getUsername()))
                .expectComplete()
                .verify();
    }

    @Test
    public void getAccountByNameTest() {
        String token = jwtUtils.generateToken(username, Arrays.asList(new SimpleGrantedAuthority(Role.getRole(1))));

        Mono<AccountLessInfoDto> request = WebClient
                .create("http://localhost:" + port + "/api/account/" + username)
                .get()
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(AccountLessInfoDto.class);

        StepVerifier
                .create(request)
                .assertNext(accountDto -> assertEquals(username, accountDto.getUsername()))
                .expectComplete()
                .verify();
    }

    @Test
    public void editAccountDataTest(){
        String token = jwtUtils.generateToken(username, Arrays.asList(new SimpleGrantedAuthority(Role.getRole(6))));
        AccountDto newData = new AccountDto();
        newData.setEmail("some.new123@mail.com");
        newData.setPremiumDays(214L);
        newData.setType(4);
        newData.setUsername(username);
        newData.setLastDay(123L);
        newData.setCreationDate(231L);

        Mono<AccountDto> request =
                WebClient.create("http://localhost:" + port + "/api/account/" + username)
                .put()
                .body(Mono.just(newData), AccountDto.class)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(AccountDto.class);

        StepVerifier.create(request)
                .assertNext(accountDto -> assertEquals(newData.getEmail(), accountDto.getEmail()))
                .expectComplete()
                .verify();
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
}
