package com.gurgaczj.jmaker.security.userdetails;

import com.gurgaczj.jmaker.repository.AccountRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AccountDetailsService implements ReactiveUserDetailsService {

    private final AccountRepository accountRepository;

    public AccountDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return accountRepository.findByUsername(username)
                .flatMap(account -> Mono.just(new AccountDetails(account)));
    }
}
