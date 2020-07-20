package com.gurgaczj.jmaker.bean.database.h2;

import com.gurgaczj.jmaker.model.Account;
import com.gurgaczj.jmaker.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Profile("dev")
public class TestDataInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public TestDataInitializer(PasswordEncoder passwordEncoder, AccountRepository accountRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }

    public static final String TEST_USER = "username";
    public static final String TEST_USER_PASS = "password";

    @Override
    public void run(String... args) throws Exception {
        Account account = new Account();
        account.setUsername(TEST_USER);
        account.setCreationDate(Instant.now().getEpochSecond());
        account.setSecret("efewfesfsfe");
        account.setEmail("safd@wpp.pl");
        account.setLastDay(3215235L);
        account.setPassword(passwordEncoder.encode(TEST_USER_PASS));
        account.setPremiumDays(325235L);
        account.setType(1);

        account = accountRepository.save(account).block();
    }
}
