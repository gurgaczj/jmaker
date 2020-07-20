package com.gurgaczj.jmaker.repository;

import com.gurgaczj.jmaker.model.Account;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

@EnableR2dbcRepositories
public interface AccountRepository extends ReactiveCrudRepository<Account, Long> {

    Mono<Account> findByUsername(String username);
}
