package com.gurgaczj.jmaker.security;

import com.gurgaczj.jmaker.h2.TestDataInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class LoginFormAuthenticationTests {
    
    @LocalServerPort
    private int port;
    
    @Test
    public void authenticationTest(){
        String username = TestDataInitializer.TEST_USER;
        String password = TestDataInitializer.TEST_USER_PASS;

        WebClient
                .create("http://localhost:" + this.port + "/login")
                .post()
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(createFormLoginData(username, password))
                .exchange()
                .as(StepVerifier::create)
                .assertNext(clientResponse -> {
                    assertEquals(clientResponse.statusCode(), HttpStatus.OK);
                    assertTrue(clientResponse.headers().header(HttpHeaders.AUTHORIZATION).size() != 0);
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void authenticationTest_wrongCredentials(){
        String username = "wrong_user";
        String password = "wrong_pass";

        WebClient
                .create("http://localhost:" + this.port + "/login")
                .post()
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(createFormLoginData(username, password))
                .exchange()
                .as(StepVerifier::create)
                .assertNext(clientResponse -> {
                    assertEquals(clientResponse.statusCode(), HttpStatus.UNAUTHORIZED);
                })
                .expectComplete()
                .verify();
    }

    private MultiValueMap<String, String> createFormLoginData(String username, String password) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", username);
        formData.add("password", password);
        return formData;
    }
}
