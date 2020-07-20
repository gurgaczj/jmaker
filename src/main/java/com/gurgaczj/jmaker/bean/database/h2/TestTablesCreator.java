package com.gurgaczj.jmaker.bean.database.h2;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.connectionfactory.init.CompositeDatabasePopulator;
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class TestTablesCreator {

    @Bean
    public ConnectionFactoryInitializer tableCreator(ConnectionFactory connectionFactory){
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}
