package com.gurgaczj.jmaker.service.impl;

import com.gurgaczj.jmaker.dto.AccountDto;
import com.gurgaczj.jmaker.exception.InternalServerException;
import com.gurgaczj.jmaker.exception.NotFoundException;
import com.gurgaczj.jmaker.exception.ValidationException;
import com.gurgaczj.jmaker.mapper.DtoMapper;
import com.gurgaczj.jmaker.model.Account;
import com.gurgaczj.jmaker.model.Email;
import com.gurgaczj.jmaker.model.NewPassword;
import com.gurgaczj.jmaker.repository.AccountRepository;
import com.gurgaczj.jmaker.service.AccountService;
import com.gurgaczj.jmaker.validator.RegisterValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final static Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final RegisterValidator registerValidator;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, RegisterValidator registerValidator, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.registerValidator = registerValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<Account> save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Mono<Account> findByUsername(String username) {
        log.info("Looking for user with username " + username);
        return accountRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new NotFoundException("Could not find account with username " + username)));
    }

    @Override
    public Flux<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Mono<Account> findByHash(String hash) {
        return accountRepository.findByHash(hash);
    }

    @Override
    public Mono<AccountDto> getAccount(Principal principal) {
        return findByUsername(principal.getName())
                .map(account -> DtoMapper.toDto(account, AccountDto.class));
    }

    @Override
    public Mono<AccountDto> getAccountByName(String accountName) {
        return findByUsername(accountName)
                .map(account -> DtoMapper.toDto(account, AccountDto.class));
    }

    @Override
    public Mono<AccountDto> editAccount(String accountName, AccountDto newData) {
        return Mono.just(accountName)
                .flatMap(account -> findByUsername(accountName))
                .flatMap(account -> setAccountData(account, newData))
                .flatMap(account -> save(account))
                .switchIfEmpty(Mono.error(new InternalServerException("Could not save new account data. Try again later or contact admin")))
                .map(account -> DtoMapper.toDto(account, AccountDto.class));
    }

    @Override
    public Mono<AccountDto> updatePassword(Principal principal, NewPassword newPassword) {
        log.info("Updating password for user " + principal.getName());
        return findByUsername(principal.getName())
                .flatMap(account -> checkPasswords(account, newPassword))
                .flatMap(account -> saveNewPassword(account, newPassword.getNewPassword()))
                .switchIfEmpty(Mono.error(new InternalServerException("Error while saving new password. Try again later or contact administrator")))
                .flatMap(account -> Mono.just(DtoMapper.toDto(account, AccountDto.class)));
    }

    @Override
    public Mono<AccountDto> editEmail(Principal principal, Email email) {
        log.info("Updating email for user " + principal.getName());
        return findByUsername(principal.getName())
                .flatMap(account -> checkEmail(account, email))
                .flatMap(account -> saveNewEmail(account, email))
                .switchIfEmpty(Mono.error(new InternalServerException("Error while saving new email. Try again later or contact administrator")))
                .flatMap(account -> Mono.just(DtoMapper.toDto(account, AccountDto.class)));
    }

    private Mono<Account> checkEmail(Account account, Email email) {
        if (!registerValidator.validateEmail(email.getEmail()))
            return Mono.error(new ValidationException("New email address does not meet requirements"));

        if (account.getEmail().equals(email.getEmail()))
            return Mono.error(new ValidationException("Old and new email address are the same"));

        return Mono.just(account);
    }

    private Mono<Account> saveNewEmail(Account account, Email email) {
        return Mono.just(account)
                .flatMap(acc -> {
                    acc.setEmail(email.getEmail());
                    return save(acc);
                });
    }

    private Mono<Account> checkPasswords(Account account, NewPassword newPassword) {
        if (!passwordEncoder.matches(newPassword.getOldPassword(), account.getPassword()))
            return Mono.error(new ValidationException("Old password does not match"));

        if (!registerValidator.validatePassword(newPassword.getNewPassword()))
            return Mono.error(new ValidationException("New password does not meet requirements"));

        if (!registerValidator.passwordsTheSame(newPassword.getNewPassword(), newPassword.getVerifyNewPassword()))
            return Mono.error(new ValidationException("Passwords are not the same"));

        return Mono.just(account);
    }

    private Mono<Account> saveNewPassword(Account account, String newPassword) {
        return Mono.just(account)
                .flatMap(acc -> {
                    acc.setPassword(passwordEncoder.encode(newPassword));
                    return Mono.just(acc);
                }).flatMap(acc -> save(acc));
    }

    private Mono<? extends Account> setAccountData(Account account, AccountDto newData) {
        account.setEmail(newData.getEmail());
        account.setUsername(newData.getUsername());
        account.setPremiumDays(newData.getPremiumDays());
        account.setType(newData.getType());
        return Mono.just(account);
    }
}
