package ch.andrewrembrandt.effectiveit.repository;

import ch.andrewrembrandt.effectiveit.dto.ProductDTO;
import ch.andrewrembrandt.effectiveit.model.OrderProduct;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderProductRepository extends R2dbcRepository<OrderProduct, Long> {

  @Query(
      "select p.sku, p.name, p.price, p.creation_date from product p inner join order_product op on "
          + "op.product_id = p.id and op.tenant_id = p.tenant_id where op.order_id = :order_id")
  Flux<ProductDTO> getProductsForOrder(Long orderId);

  @Modifying
  @Query(
      "insert into order_product(order_id, product_id) select :order_id, p.id from product p where p.sku = :sku")
  Mono<Integer> addProductForOrder(Long orderId, String sku);
}
