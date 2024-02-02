package ch.andrewrembrandt.effectiveit.service;

import ch.andrewrembrandt.effectiveit.dto.ProductDTO;
import ch.andrewrembrandt.effectiveit.dto.ProductDataDTO;
import ch.andrewrembrandt.effectiveit.mapper.ProductDataMapper;
import ch.andrewrembrandt.effectiveit.mapper.ProductMapper;
import ch.andrewrembrandt.effectiveit.repository.ProductRepository;
import ch.andrewrembrandt.effectiveit.tenantaware.TenantContext;
import ch.andrewrembrandt.effectiveit.util.SkuAlreadyExistsException;
import ch.andrewrembrandt.effectiveit.util.SkuNotFoundException;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ProductService {
  private final ProductRepository repo;
  private final ProductDataMapper dataMapper;
  private final ProductMapper mapper;

  public Flux<ProductDTO> getAllActiveProducts() {
    return Flux.deferContextual(
        ctx -> repo.findAllActiveProducts(TenantContext.tenantId(ctx)).map(mapper::toProductDto));
  }

  public Mono<Void> deleteProduct(String sku) {
    return Mono.deferContextual(
        ctx ->
            repo.softDeleteBySku(sku, TenantContext.tenantId(ctx))
                .flatMap(ensureSingleUpdate(sku, false)));
  }

  public Mono<Void> createProduct(String sku, ProductDataDTO dto) {
    val newProduct = dataMapper.toProduct(dto);
    newProduct.setSku(sku);
    return repo.save(newProduct).then();
  }

  public Mono<Void> updateProduct(String sku, ProductDataDTO dto) {
    return Mono.deferContextual(ctx -> repo.findBySku(sku, TenantContext.tenantId(ctx)))
        .flatMap(
            existingProduct -> {
              val newProduct = dataMapper.toProduct(dto);
              newProduct.setId(existingProduct.getId());
              newProduct.setSku(sku);
              return repo.save(newProduct);
            })
        .then();
  }

  private Function<Integer, Mono<? extends Void>> ensureSingleUpdate(String sku, boolean create) {
    return numUpdated -> {
      if (numUpdated == 1) return Mono.empty();
      else {
        if (create) return Mono.error(new SkuAlreadyExistsException(sku));
        else return Mono.error(new SkuNotFoundException(sku));
      }
    };
  }
}
