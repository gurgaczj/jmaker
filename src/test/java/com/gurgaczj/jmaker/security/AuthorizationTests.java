package com.gurgaczj.jmaker.security;

import com.gurgaczj.jmaker.h2.TestDataInitializer;
import com.gurgaczj.jmaker.jwt.JwtUtils;
import com.gurgaczj.jmaker.model.Role;
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

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class AuthorizationTests {

    private JwtUtils jwtUtils;
    @LocalServerPort
    private int port;

    @Test
    public void auhorizationTest(){
        String username = TestDataInitializer.TEST_USER;
        String role = Role.getRole(1);
        String token = jwtUtils.generateToken(username, Collections.singletonList(new SimpleGrantedAuthority(role)));

        token = String.join(" ", "Bearer", token);

        Mono<ClientResponse> response = WebClient
                .create("http://localhost:" + port + "/test1")
                .get()
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchange();

        Mono<String> body = response.flatMap(clientResponse -> clientResponse.bodyToMono(String.class));

        StepVerifier.create(response)
                .assertNext(clientResponse -> assertEquals(HttpStatus.OK, clientResponse.statusCode()))
                .expectComplete()
                .verify();

        StepVerifier.create(body)
                .assertNext(bodyUsername -> assertEquals(username, bodyUsername))
                .expectComplete()
                .verify();
    }

    @Test
    public void anonymousAccessTestToPublicEndpoint(){
        Mono<ClientResponse> response = WebClient
                .create("http://localhost:" + port + "/test2")
                .get()
                .exchange();

        StepVerifier.create(response)
                .assertNext(clientResponse -> assertEquals(HttpStatus.OK, clientResponse.statusCode()))
                .expectComplete()
                .verify();

        StepVerifier.create(response.flatMap(clientResponse -> clientResponse.bodyToMono(String.class)))
                .assertNext(username -> assertEquals("anonymousUser", username))
                .expectComplete()
                .verify();
    }

    @Test
    public void anonymousAccessTestToProtectedEndpoint(){
        Mono<ClientResponse> response = WebClient
                .create("http://localhost:" + port + "/test1")
                .get()
                .exchange();

        StepVerifier.create(response)
                .assertNext(clientResponse -> assertEquals(HttpStatus.FORBIDDEN, clientResponse.statusCode()))
                .expectComplete()
                .verify();

        StepVerifier.create(response.flatMap(clientResponse -> clientResponse.bodyToMono(String.class)))
                .assertNext(username -> assertEquals("Access Denied", username))
                .expectComplete()
                .verify();
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
}
