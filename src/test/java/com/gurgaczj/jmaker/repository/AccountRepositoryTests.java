package com.gurgaczj.jmaker.repository;

import com.gurgaczj.jmaker.model.Account;
import com.gurgaczj.jmaker.service.AccountService;
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
@ActiveProfiles("test")
public class AccountRepositoryTests {

    private AccountService accountService;

    @Test
    public void saveTest() {
        Account account = new Account();
        account.setId(null);
        account.setUsername("TEST_1");
        account.setCreationDate(Instant.now().getEpochSecond());
        account.setSecret("efewfesfs56u56e");
        account.setEmail("sa56ikjfd@wpp.pl");
        account.setLastDay(3215235L);
        account.setPassword("pass67ih");
        account.setPremiumDays(325235L);
        account.setEnabled(true);
        account.setType(1);

        Mono<Account> res1 = accountService.save(account);

        StepVerifier.create(res1)
                .assertNext(account1 -> {
                    assertEquals(account1.getEmail(), account.getEmail());
                    assertNotNull(account1.getId());
                })
                .expectComplete()
                .verify();

        StepVerifier.create(accountService.findAll().collectList())
                .consumeNextWith(accounts -> System.out.println("There are " + accounts.size() + " accounts"))
                .verifyComplete();
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
