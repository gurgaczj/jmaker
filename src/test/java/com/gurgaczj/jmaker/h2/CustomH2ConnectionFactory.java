package com.gurgaczj.jmaker.h2;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
@Profile({"test"})
public class CustomH2ConnectionFactory extends AbstractR2dbcConfiguration {

    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        return new H2ConnectionFactory(
                H2ConnectionConfiguration.builder()
                        .url("mem:testdb;DB_CLOSE_DELAY=-1;")
                        .username("sa")
                        .build()
        );
//        return new H2ConnectionFactory(H2ConnectionConfiguration.builder()
//                .inMemory("testdb")
//                .username("sa")
//                .property(H2ConnectionOption.DB_CLOSE_DELAY, "-1")
//                .property(H2ConnectionOption.DB_CLOSE_ON_EXIT, "false")
//                .build());
    }

    @Bean
    public DatabaseClient h2DBClient(){
        return DatabaseClient
                .builder()
                .connectionFactory(connectionFactory())
                .build();
    }


}
