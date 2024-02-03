package ch.andrewrembrandt.effectiveit.repository;

import ch.andrewrembrandt.effectiveit.model.Product;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends R2dbcRepository<Product, Long> {
  @Query("select * from product p where p.deleted = false")
  Flux<Product> findAllActiveProducts();

  @Modifying
  @Query("update product p set p.deleted=true where p.sku=:sku and p.deleted=false")
  Mono<Integer> softDeleteBySku(String sku);

  @Query("select * from product p where p.sku = :sku and p.deleted=false")
  Mono<Product> findBySku(String sku);
}
