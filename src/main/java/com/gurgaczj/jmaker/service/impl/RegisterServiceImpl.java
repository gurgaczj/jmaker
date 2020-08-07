package com.gurgaczj.jmaker.service.impl;

import com.gurgaczj.jmaker.dto.AccountDto;
import com.gurgaczj.jmaker.exception.MailSenderException;
import com.gurgaczj.jmaker.exception.RegisterException;
import com.gurgaczj.jmaker.mail.EmailService;
import com.gurgaczj.jmaker.model.Account;
import com.gurgaczj.jmaker.model.MailSendingParams;
import com.gurgaczj.jmaker.model.Register;
import com.gurgaczj.jmaker.service.AccountService;
import com.gurgaczj.jmaker.service.DtoMapper;
import com.gurgaczj.jmaker.service.RegisterService;
import com.gurgaczj.jmaker.validator.register.Validator;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class RegisterServiceImpl implements RegisterService {

    private final static Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    @Value("${jmaker.account.require-verification}")
    private boolean shouldSendActivationMail;
    @Value("${jmaker.transfer-protocol}")
    private String protocol;
    @Value("${jmaker.host}")
    private String host;
    @Value("${jmaker.account.verification-path}")
    private String path;
    @Value("${jmaker.account.verification-param}")
    private String param;

    private final Validator<Register, Register> validator;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final EmailService emailService;

    public RegisterServiceImpl(Validator<Register, Register> validator,
                               @Qualifier("sha1PasswordEncoder") PasswordEncoder passwordEncoder,
                               AccountService accountService, EmailService emailService) {
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
        this.emailService = emailService;
    }

    @Override
    public Mono<AccountDto> verifyAccount(String verificationCode) {
        return Mono.just(verificationCode)
                .flatMap(code -> accountService.findByHash(code))
                .switchIfEmpty(Mono.error(new RegisterException("Cannot verify this account. Make sure you have good url")))
                .flatMap(account ->
                        account.isEnabled() ? Mono.error(new RegisterException("Your account has been verified")) :
                                setAccountEnabled(account)
                )
                .flatMap(account -> accountService.save(account))
                .flatMap(account -> toDto(account));
    }

    private Mono<Account> setAccountEnabled(Account account) {
        account.setEnabled(true);
        return Mono.just(account);
    }

    @Override
    public Mono<AccountDto> register(Register register) {
        return Mono.just(register)
                .flatMap(registerModel -> validator.validate(registerModel))
                .map(registerModel -> createAccount(registerModel))
                .flatMap(account -> accountService.save(account))
                .flatMap(account -> checkMailSending(account))
                .switchIfEmpty(Mono.error(
                        new MailSenderException(
                                "Error during email verification sending. Contact administration about this.")))
                //.switchIfEmpty(Mono.error(RegisterException::new))
                .flatMap(account -> toDto(account));
    }

    private Mono<Account> checkMailSending(Account acc) {
        if (shouldSendActivationMail) {
            try {
                emailService.sendSimpleMessage(createAccountVerificationMessage(acc));
            } catch (MailException e) {
                logger.error(e.getMessage());
                return Mono.empty();
            }
        }
        return Mono.just(acc);
    }

    private MailSendingParams createAccountVerificationMessage(Account acc) {
        MailSendingParams mailSendingParams = new MailSendingParams();
        mailSendingParams.setTo(acc.getEmail());
        mailSendingParams.setSubject("Verify your account");
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(protocol)
                .host(host)
                .path(path)
                .queryParam(param, acc.getHash())
                .build();
        mailSendingParams.setText(uriComponents.toUriString());
        System.out.println(mailSendingParams);
        return mailSendingParams;
    }

    private Mono<AccountDto> toDto(Account account) {
//        return Mono.just(account)
//                .map(acc -> {
//                    ModelMapper modelMapper = new ModelMapper();
//                    return modelMapper.map(acc, AccountDto.class);
//                });
        return Mono.just(DtoMapper.toDto(account, AccountDto.class));
    }

    private Account createAccount(Register registerModel) {
        Account account = new Account();
        account.setUsername(registerModel.getUsername());
        account.setCreationDate(Instant.now().getEpochSecond());
        account.setEmail(registerModel.getEmail());
        account.setLastDay(0L);
        account.setPassword(passwordEncoder.encode(registerModel.getPassword()));
        account.setPremiumDays(0L);
        account.setType(1);
        account.setHash(DigestUtils.sha1Hex(registerModel.getEmail().getBytes()));
        if (!shouldSendActivationMail)
            account.setEnabled(true);

        return account;
    }
}
