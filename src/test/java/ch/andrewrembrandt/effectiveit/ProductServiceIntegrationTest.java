package ch.andrewrembrandt.effectiveit;

import static org.assertj.core.api.Assertions.assertThat;

import ch.andrewrembrandt.effectiveit.dto.ProductDataDTO;
import ch.andrewrembrandt.effectiveit.repository.ProductRepository;
import ch.andrewrembrandt.effectiveit.service.ProductService;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

@SpringBootTest
@Import(R2dbcIntegrationTestInitialiser.class)
public class ProductServiceIntegrationTest extends R2dbcIntegrationTestInitialiser
    implements BaseTenantAwareTest {
  @Autowired ProductRepository productRepo;
  @Autowired ProductService productService;

  @Test
  void addProduct() {
    val newProduct = new ProductDataDTO("Gipfeli", new BigDecimal("200.5"), LocalDate.now());

    StepVerifier.create(
            addTenant(productService.createProduct("C1", newProduct), 1l)
                .then(productRepo.findAllActiveProducts(1).collectList()))
        .consumeNextWith(p -> assertThat(p).isNotEmpty())
        .verifyComplete();
  }
}
