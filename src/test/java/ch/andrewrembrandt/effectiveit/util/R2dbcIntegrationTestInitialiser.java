package ch.andrewrembrandt.effectiveit.util;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Slf4j
public class R2dbcIntegrationTestInitialiser {
  private static boolean initialised = false;

  @Autowired
  void initialiseDb(ConnectionFactory connectionFactory) {
    if (!initialised) {
      val initializer = new ConnectionFactoryInitializer();
      initializer.setConnectionFactory(connectionFactory);

      val populator = new CompositeDatabasePopulator();
      populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
      populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")));
      initializer.setDatabasePopulator(populator);
      log.info("Configured schema.sql and data.sql for R2DBC tests");
      initialised = true;
    }
  }
}
