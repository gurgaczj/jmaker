package com.gurgaczj.jmaker.service;

import com.gurgaczj.jmaker.model.Account;
import com.gurgaczj.jmaker.repository.AccountRepository;
import com.gurgaczj.jmaker.service.impl.AccountServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {

    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        accountService = new AccountServiceImpl(accountRepository);
    }

    @Test
    public void saveTest() {
        Account account = createAccount();

        Mockito.when(accountRepository.save(account)).thenAnswer(invocation -> {
            Account account1 = invocation.getArgument(0);
            account1.setId(1L);
            return Mono.just(account);
        });

        Account res = accountService.save(account).block();

        assertEquals(account, res);
    }

    @Test
    public void findByUsernameTest() {
        Account account = createAccount();

        Mockito.when(accountRepository.findByUsername(account.getUsername())).thenReturn(Mono.just(account));

        Account result = accountService.findByUsername(account.getUsername()).block();

        assertEquals(account.getUsername(), result.getUsername());
    }

    @Test
    public void findAllTest() {
        Account account1 = createAccount();

        Account account2 = createAccount();
        account2.setUsername("username2");

        Mockito.when(accountRepository.findAll()).thenReturn(Flux.just(account1, account2));

        StepVerifier
                .create(accountService.findAll())
                .expectNext(account1)
                .expectNext(account2)
                .expectComplete()
                .verify();
    }

    @Test
    public void findByHashTest() {
        Account account = createAccount();

        Mockito.when(accountRepository.findByHash(account.getHash())).thenReturn(Mono.just(account));

        Account result = accountService.findByHash(account.getHash()).block();

        assertEquals(account, result);
    }

    private Account createAccount() {
        Account account = new Account();
        account.setUsername("username");
        account.setCreationDate(Instant.now().getEpochSecond());
        account.setEmail("some@mail.com");
        account.setLastDay(0L);
        account.setPassword("password");
        account.setPremiumDays(0L);
        account.setType(1);
        account.setHash(DigestUtils.sha1Hex(account.getEmail().getBytes()));
        return account;
    }
}
