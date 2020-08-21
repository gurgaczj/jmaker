package com.gurgaczj.jmaker.service;

import com.gurgaczj.jmaker.dto.AccountDto;
import com.gurgaczj.jmaker.model.Account;
import com.gurgaczj.jmaker.model.Email;
import com.gurgaczj.jmaker.model.NewPassword;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

public interface AccountService {

    Mono<Account> save(Account account);

    Mono<Account> findByUsername(String username);

    Flux<Account> findAll();

    Mono<Account> findByHash(String hash);

    Mono<AccountDto> getAccount(Principal principal);

    Mono<AccountDto> getAccountByName(String accountName);

    Mono<AccountDto> editAccount(String accountName, AccountDto accountData);

    Mono<AccountDto> updatePassword(Principal principal, NewPassword newPassword);

    Mono<AccountDto> editEmail(Principal principal, Email email);
}
