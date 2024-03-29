package ch.andrewrembrandt.effectiveit;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
@EnableR2dbcRepositories
@Slf4j
public class PopulateDbConfiguration {

  @Bean
  ConnectionFactoryInitializer initialiser(ConnectionFactory connectionFactory) {
    var initialiser = new ConnectionFactoryInitializer();
    initialiser.setConnectionFactory(connectionFactory);
    var populator = new CompositeDatabasePopulator();
    populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
    populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")));
    initialiser.setDatabasePopulator(populator);
    log.info("Configured schema.sql and data.sql for database initialisation.");
    return initialiser;
  }
}
