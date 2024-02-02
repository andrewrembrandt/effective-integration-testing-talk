package ch.andrewrembrandt.effectiveit;

import io.r2dbc.spi.ConnectionFactory;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

class R2dbcIntegrationTestInitialiser {
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
      initialised = true;
    }
  }
}
