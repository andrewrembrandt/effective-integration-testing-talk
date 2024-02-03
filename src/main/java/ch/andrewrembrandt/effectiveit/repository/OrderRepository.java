package ch.andrewrembrandt.effectiveit.repository;

import ch.andrewrembrandt.effectiveit.model.CustomerOrder;
import java.time.ZonedDateTime;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository extends R2dbcRepository<CustomerOrder, Long> {
  Flux<CustomerOrder> findByPlacedTimeBetween(ZonedDateTime from, ZonedDateTime to);
}
