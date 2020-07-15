package com.gurgaczj.jmaker.repository;

import com.gurgaczj.jmaker.model.Account;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

@EnableR2dbcRepositories
public interface AccountRepository extends ReactiveCrudRepository<Account, Long> {

}
