package ch.andrewrembrandt.effectiveit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import ch.andrewrembrandt.effectiveit.mapper.ProductDataMapper;
import ch.andrewrembrandt.effectiveit.mapper.ProductDataMapperImpl;
import ch.andrewrembrandt.effectiveit.mapper.ProductMapperImpl;
import ch.andrewrembrandt.effectiveit.model.Product;
import ch.andrewrembrandt.effectiveit.repository.ProductRepository;
import ch.andrewrembrandt.effectiveit.service.ProductService;
import java.math.BigDecimal;
import lombok.val;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@Import({ProductService.class, ProductMapperImpl.class, ProductDataMapperImpl.class})
@Disabled
public class ProductServiceTest {
  @MockBean ProductRepository productRepo;
  @Autowired ProductService productService;
  @Autowired ProductDataMapper productDataMapper;

  @Test
  @Disabled
  void addProduct() {
    val newProduct = new Product(null, "C1", "Gipfeli", new BigDecimal("200.5"), null, null);

    Mockito.when(productRepo.save(any())).thenReturn(Mono.empty());

    val createMono =
        productService.createProduct("C1", productDataMapper.toProductDataDto(newProduct));
    StepVerifier.create(createMono).verifyComplete();

    verify(productRepo).save(eq(newProduct));
  }
}
