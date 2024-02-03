package ch.andrewrembrandt.effectiveit.service;

import ch.andrewrembrandt.effectiveit.dto.NewOrderDTO;
import ch.andrewrembrandt.effectiveit.dto.OrderDTO;
import ch.andrewrembrandt.effectiveit.dto.ProductDTO;
import ch.andrewrembrandt.effectiveit.model.CustomerOrder;
import ch.andrewrembrandt.effectiveit.repository.OrderProductRepository;
import ch.andrewrembrandt.effectiveit.repository.OrderRepository;
import ch.andrewrembrandt.effectiveit.util.NoProductsException;
import ch.andrewrembrandt.effectiveit.util.SkuNotFoundException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.val;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class OrderService {
  private final OrderRepository orderRepo;
  private final OrderProductRepository orderProductRepo;

  public Flux<OrderDTO> getOrdersBetween(ZonedDateTime from, ZonedDateTime to) {
    return Flux.deferContextual(
        ctx ->
            orderRepo
                .findByPlacedTimeBetweenOrderByPlacedTime(from, to)
                .flatMap(retrieveProducts()));
  }

  public Mono<Void> createOrder(NewOrderDTO dto) {
    val checkedProducts = ensureProducts(dto);
    val order = new CustomerOrder(null, dto.getBuyerEmail(), ZonedDateTime.now());
    return checkedProducts
        .flatMap(na -> orderRepo.save(order))
        .flatMapMany(
            savedOrder ->
                Flux.fromIterable(dto.getProductSkus())
                    .flatMap(sku -> orderProductRepo.addProductForOrder(savedOrder.getId(), sku)))
        .reduce(0, (acc, num) -> acc + num)
        .flatMap(
            numProductsAddedToOrder -> {
              if (numProductsAddedToOrder == dto.getProductSkus().size()) return Mono.empty();
              else return Mono.error(new SkuNotFoundException(dto.toString()));
            })
        .then();
  }

  private Mono<Boolean> ensureProducts(NewOrderDTO dto) {
    if (dto.getProductSkus().isEmpty()) return Mono.error(new NoProductsException(dto.toString()));
    else return Mono.just(false);
  }

  private Function<CustomerOrder, Publisher<? extends OrderDTO>> retrieveProducts() {
    return oe ->
        Mono.deferContextual(
            ctx ->
                orderProductRepo
                    .getProductsForOrder(oe.getId())
                    .collectList()
                    .map(
                        products ->
                            new OrderDTO(
                                oe.getId(),
                                products,
                                oe.getBuyerEmail(),
                                oe.getPlacedTime(),
                                products.stream()
                                    .map(ProductDTO::getPrice)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add))));
  }
}
