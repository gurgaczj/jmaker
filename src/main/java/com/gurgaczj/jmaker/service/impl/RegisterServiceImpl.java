package com.gurgaczj.jmaker.service.impl;

import com.gurgaczj.jmaker.dto.AccountDto;
import com.gurgaczj.jmaker.model.Account;
import com.gurgaczj.jmaker.model.Register;
import com.gurgaczj.jmaker.service.AccountService;
import com.gurgaczj.jmaker.service.RegisterService;
import com.gurgaczj.jmaker.validator.register.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class RegisterServiceImpl implements RegisterService {

    private final Validator<Register, Register> validator;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    public RegisterServiceImpl(Validator validator,
                               @Qualifier("sha1PasswordEncoder") PasswordEncoder passwordEncoder, AccountService accountService) {
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
    }

    @Override
    public Mono<AccountDto> register(Register register) {
        return Mono.just(register)
                .flatMap(r -> validator.validate(r))
                .map(this::createAccount)
                .flatMap(accountService::save)
                .flatMap(this::toDto);
    }

    private Mono<AccountDto> toDto(Account account) {
        return Mono.just(account)
                .map(acc -> {
                    ModelMapper modelMapper = new ModelMapper();
                    return modelMapper.map(acc, AccountDto.class);
                });
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

        return account;
    }
}
