package com.gurgaczj.jmaker.service;

import com.gurgaczj.jmaker.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {

    Mono<Account> save(Account account);

    Mono<Account> findByUsername(String username);

    Flux<Account> findAll();

    Mono<Account> findByHash(String hash);

    //TODO: add toDto method
}
