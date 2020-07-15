package com.gurgaczj.jmaker.bean.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;

@Component
@Profile("test")
public class TestTablesCreator implements CommandLineRunner {

    private final CustomH2ConnectionFactory customH2ConnectionFactory;

    public TestTablesCreator(CustomH2ConnectionFactory customH2ConnectionFactory) {
        this.customH2ConnectionFactory = customH2ConnectionFactory;
    }

    @Override
    public void run(String... args) throws Exception {
        DatabaseClient client = DatabaseClient.create(customH2ConnectionFactory.connectionFactory());
        File file = new ClassPathResource("schema.sql").getFile();

        String sql = new String(Files.readAllBytes(file.toPath()));

        client.execute(sql)
                .fetch()
                .rowsUpdated()
                .as(reactor.test.StepVerifier::create)
                .expectNext(4)
                .verifyComplete();
    }
}
