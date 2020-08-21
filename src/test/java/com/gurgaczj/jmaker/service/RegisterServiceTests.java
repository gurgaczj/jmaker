package com.gurgaczj.jmaker.service;

import com.gurgaczj.jmaker.dto.AccountDto;
import com.gurgaczj.jmaker.exception.MailSenderException;
import com.gurgaczj.jmaker.exception.RegisterException;
import com.gurgaczj.jmaker.mail.EmailService;
import com.gurgaczj.jmaker.model.Account;
import com.gurgaczj.jmaker.model.MailSendingParams;
import com.gurgaczj.jmaker.model.Register;
import com.gurgaczj.jmaker.security.encoder.Sha1PasswordEncoder;
import com.gurgaczj.jmaker.service.impl.RegisterServiceImpl;
import com.gurgaczj.jmaker.validator.register.RegisterValidatorImpl;
import com.gurgaczj.jmaker.validator.RegisterValidator;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RegisterServiceTests {

    @Mock
    private RegisterValidator<Register, Register> registerValidator = new RegisterValidatorImpl();
    private PasswordEncoder passwordEncoder = new Sha1PasswordEncoder();
    @Mock
    private AccountService accountService;
    @Mock
    private EmailService emailService;

    private RegisterServiceImpl registerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        registerService = new RegisterServiceImpl(registerValidator, passwordEncoder, accountService, emailService);
    }

    @Test
    public void registerTest() {
        Register register = RegisterServiceTests.createRegisterModel();

        Mockito.when(registerValidator.validate(register)).thenReturn(Mono.just(register));
        Mockito.when(accountService.save(any(Account.class))).thenAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            account.setId(1L);
            return Mono.just(account);
        });

        AccountDto accountDto = registerService.register(register).block();
        assertEquals(register.getUsername(), Objects.requireNonNull(accountDto).getUsername());
        assertEquals(register.getEmail(), accountDto.getEmail());
        assertNotNull(accountDto.getType());
        assertNotNull(accountDto.getPremiumDays());
        assertNotNull(accountDto.getLastDay());
        assertNotNull(accountDto.getCreationDate());
    }

    @Test
    public void registerTest_validatorThrowError() {
        Register register = RegisterServiceTests.createRegisterModel();

        Mockito.when(registerValidator.validate(register)).thenReturn(Mono.error(new RegisterException()));

        StepVerifier.create(registerService.register(register))
                .expectError(RegisterException.class)
                .verify();
    }

    @Test
    public void registerTest_repoSaveError() {
        Register register = RegisterServiceTests.createRegisterModel();
        DataAccessException exception = new DataIntegrityViolationException("asd");

        Mockito.when(registerValidator.validate(register)).thenReturn(Mono.just(register));
        Mockito.when(accountService.save(any(Account.class))).thenReturn(Mono.error(exception));

        StepVerifier.create(registerService.register(register))
                .expectError(DataIntegrityViolationException.class)
                .verify();
    }

    @Test
    public void registerTest_mailSenderThrowsError() {
        Register register = RegisterServiceTests.createRegisterModel();

        Mockito.when(registerValidator.validate(register)).thenReturn(Mono.just(register));
        Mockito.when(accountService.save(any(Account.class))).thenAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            account.setId(1L);
            return Mono.just(account);
        });
        Mockito.doThrow(MailSendException.class).when(emailService).sendSimpleMessage(any(MailSendingParams.class));

        try {
            setField("shouldSendActivationMail", true);
            setField("protocol", "http");
            setField("host", "host");
            setField("path", "/path");
            setField("param", "someParam");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        StepVerifier.create(registerService.register(register))
                .expectError(MailSenderException.class)
                .verify();
    }

    @Test
    public void verificationTest() {
        Account account = createAccount();

        Mockito.when(accountService.findByHash(account.getHash())).thenReturn(Mono.just(account));
        Mockito.when(accountService.save(any(Account.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(registerService.verifyAccount(account.getHash()))
                .assertNext(accountDto -> {
                    assertNotNull(accountDto);
                    assertEquals(account.getUsername(), accountDto.getUsername());
                    assertEquals(account.getEmail(), accountDto.getEmail());
                    assertEquals(account.getCreationDate(), accountDto.getCreationDate());
                    assertEquals(account.getLastDay(), accountDto.getLastDay());
                    assertEquals(account.getPremiumDays(), accountDto.getPremiumDays());
                    assertEquals(account.getType(), accountDto.getType());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void verificationTest_cannotFindAccountByHash() {
        Account account = createAccount();

        Mockito.when(accountService.findByHash(account.getHash())).thenReturn(Mono.empty());

        StepVerifier.create(registerService.verifyAccount(account.getHash()))
                .expectError(RegisterException.class)
                .verify();
    }

    @Test
    public void verificationTest_accountAlreadyActivated() {
        Account account = createAccount();
        account.setEnabled(true);

        Mockito.when(accountService.findByHash(account.getHash())).thenReturn(Mono.just(account));

        StepVerifier.create(registerService.verifyAccount(account.getHash()))
                .expectError(RegisterException.class)
                .verify();
    }

    private Account createAccount() {
        Account account = new Account();
        account.setUsername("username");
        account.setCreationDate(Instant.now().getEpochSecond());
        account.setEmail("some@mail.com");
        account.setLastDay(0L);
        account.setPassword(passwordEncoder.encode("password"));
        account.setPremiumDays(0L);
        account.setType(1);
        account.setHash(DigestUtils.sha1Hex(account.getEmail().getBytes()));
        return account;
    }

    private void setField(String fieldName, Object value) throws NoSuchFieldException {
        FieldSetter.setField(registerService, registerService.getClass().getDeclaredField(fieldName), value);
    }

    public static Register createRegisterModel() {
        Register register = new Register();
        register.setUsername("username");
        register.setEmail("some@mail.com");
        register.setPassword("Password1!");
        register.setVerifyPassword("Password1!");
        return register;
    }
}
