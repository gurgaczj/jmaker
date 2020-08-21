package com.gurgaczj.jmaker.service;

import com.gurgaczj.jmaker.model.Account;
import com.gurgaczj.jmaker.model.NewPassword;
import com.gurgaczj.jmaker.repository.AccountRepository;
import com.gurgaczj.jmaker.service.impl.AccountServiceImpl;
import com.gurgaczj.jmaker.validator.register.PasswordValidator;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.security.auth.x500.X500Principal;
import java.security.Principal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordValidator passwordValidator;
    @Mock
    private PasswordEncoder passwordEncoder;

    private AccountService accountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        accountService = new AccountServiceImpl(accountRepository, passwordValidator, passwordEncoder);
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

    @Test
    public void testEditPassword() {
        Principal principal = new X500Principal("CN=username, OU=Jmaker, O=Jmaker, C=PL");
        Account account = createAccount();
        NewPassword newPassword = new NewPassword("password", "newPassword", "newPassword");

        Mockito.when(accountRepository.findByUsername(principal.getName())).thenReturn(Mono.just(account));
        Mockito.when(passwordEncoder.matches(newPassword.getOldPassword(), account.getPassword())).thenReturn(true);
        Mockito.when(passwordEncoder.encode(any(String.class))).thenAnswer(invocation -> DigestUtils.sha1Hex((String) invocation.getArgument(0)));
        Mockito.when(passwordValidator.validatePassword(any(String.class))).thenReturn(true);
        Mockito.when(passwordValidator.passwordsTheSame(newPassword.getNewPassword(), newPassword.getVerifyNewPassword())).thenReturn(true);
        Mockito.when(accountRepository.save(any(Account.class))).thenAnswer((Answer<Mono<Account>>) invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(accountService.updatePassword(principal, newPassword))
                .assertNext(s -> assertEquals("ok", s))
                .expectComplete()
                .verify();
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
