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
  @Query("select * from product p where p.deleted = false and p.tenant_id = :tenant_id")
  Flux<Product> findAllActiveProducts(long tenantId);

  @Modifying
  @Query("update product p set p.deleted=true where p.sku=:sku and p.deleted=false and p.tenant_id=:tenant_id")
  Mono<Integer> softDeleteBySku(String sku, long tenantId);

  @Query("select * from product p where p.sku = :sku and p.deleted=false and p.tenant_id = :tenant_id")
  Mono<Product> findBySku(String sku, long tenantId);
}
