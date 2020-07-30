package com.gurgaczj.jmaker.controller.account;

import com.gurgaczj.jmaker.dto.AccountDto;
import com.gurgaczj.jmaker.h2.TestDataInitializer;
import com.gurgaczj.jmaker.model.Register;
import com.gurgaczj.jmaker.repository.AccountRepository;
import com.gurgaczj.jmaker.validator.register.RegisterValidatorTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AccountControllerTests {

    @LocalServerPort
    private int port;

    private AccountRepository accountRepository;

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Test
    public void registerTest(){
        Register register = RegisterValidatorTest.getRegisterModel();
        register.setUsername("userName123");

        WebClient
                .create("http://localhost:" + port + "/api/account/register")
                .post()
                .body(Mono.just(register), register.getClass())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(AccountDto.class))
                .as(StepVerifier::create)
                .assertNext(accountDto ->
                        assertEquals(register.getUsername(), accountDto.getUsername()))
                .expectComplete()
                .verify();
    }

    @Test
    public void registerTest_passwordNotMeetRequirements(){
        Register register = RegisterValidatorTest.getRegisterModel();
        register.setPassword("pass");

        WebClient
                .create("http://localhost:" + port + "/api/account/register")
                .post()
                .body(Mono.just(register), register.getClass())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .as(StepVerifier::create)
                .assertNext(clientResponse ->{
                        assertEquals(HttpStatus.BAD_REQUEST, clientResponse.statusCode());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void registerTest_passwordNotTheSame(){
        Register register = RegisterValidatorTest.getRegisterModel();
        register.setVerifyPassword("password1!");

        WebClient
                .create("http://localhost:" + port + "/api/account/register")
                .post()
                .body(Mono.just(register), register.getClass())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .as(StepVerifier::create)
                .assertNext(clientResponse ->{
                    assertEquals(HttpStatus.BAD_REQUEST, clientResponse.statusCode());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void registerTest_wrongEmailFormat(){
        Register register = RegisterValidatorTest.getRegisterModel();
        register.setEmail("mail-user@.pl");

        WebClient
                .create("http://localhost:" + port + "/api/account/register")
                .post()
                .body(Mono.just(register), register.getClass())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .as(StepVerifier::create)
                .assertNext(clientResponse ->{
                    assertEquals(HttpStatus.BAD_REQUEST, clientResponse.statusCode());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void registerTest_usernameAlreadyExist(){
        Register register = RegisterValidatorTest.getRegisterModel();
        register.setUsername(TestDataInitializer.TEST_USER);

        WebClient
                .create("http://localhost:" + port + "/api/account/register")
                .post()
                .body(Mono.just(register), register.getClass())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .as(StepVerifier::create)
                .assertNext(clientResponse ->{
                    assertEquals(HttpStatus.BAD_REQUEST, clientResponse.statusCode());
                })
                .expectComplete()
                .verify();
    }
}
