package com.gurgaczj.jmaker.repository;

import com.gurgaczj.jmaker.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class AccountRepositoryTests {

    private AccountRepository accountRepository;

    @Test
    public void saveTest(){
        Account account = new Account();
        account.setUsername("asdasd456u");
        account.setCreationDate(Instant.now().getEpochSecond());
        account.setSecret("efewfesfs56u56jfe");
        account.setEmail("sa56ikjfd@wpp.pl");
        account.setLastDay(3215235L);
        account.setPassword("pass67ih");
        account.setPremiumDays(325235L);
        account.setType(1);

        Mono<Account> result = accountRepository.save(account);

        StepVerifier.create(result)
                .assertNext(account1 -> {
                    assertEquals(account1.getEmail(), account.getEmail());
                    assertNotNull(account1.getId());
                })
                .expectComplete()
                .verify();
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
