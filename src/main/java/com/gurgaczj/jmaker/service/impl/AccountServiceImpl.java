package com.gurgaczj.jmaker.service.impl;

import com.gurgaczj.jmaker.dto.AccountDto;
import com.gurgaczj.jmaker.dto.AccountLessInfoDto;
import com.gurgaczj.jmaker.exception.InternalServerException;
import com.gurgaczj.jmaker.exception.NotFoundException;
import com.gurgaczj.jmaker.mapper.DtoMapper;
import com.gurgaczj.jmaker.model.Account;
import com.gurgaczj.jmaker.repository.AccountRepository;
import com.gurgaczj.jmaker.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<Account> save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Mono<Account> findByUsername(String username) {
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
    public Mono<AccountLessInfoDto> getAccountByName(String accountName) {
        return findByUsername(accountName)
                .map(account -> DtoMapper.toDto(account, AccountLessInfoDto.class));
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

    private Mono<? extends Account> setAccountData(Account account, AccountDto newData) {
        account.setEmail(newData.getEmail());
        account.setUsername(newData.getUsername());
        account.setPremiumDays(newData.getPremiumDays());
        account.setType(newData.getType());
        return Mono.just(account);
    }
}
