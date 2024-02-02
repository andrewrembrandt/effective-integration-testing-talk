package ch.andrewrembrandt.effectiveit.repository;

import ch.andrewrembrandt.effectiveit.model.Order;
import java.time.ZonedDateTime;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository extends R2dbcRepository<Order, Long> {
  Flux<Order> findByPlacedTimeBetweenAndTenantId(ZonedDateTime from, ZonedDateTime to, Long tenantId);
}
