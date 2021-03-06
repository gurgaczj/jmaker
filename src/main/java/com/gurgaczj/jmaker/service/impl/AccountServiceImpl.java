package com.gurgaczj.jmaker.service.impl;

import com.gurgaczj.jmaker.model.Account;
import com.gurgaczj.jmaker.repository.AccountRepository;
import com.gurgaczj.jmaker.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
//    @Transactional
    public Mono<Account> save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Mono<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    @Override
    public Flux<Account> findAll() {
        return accountRepository.findAll();
    }
}
