package ch.andrewrembrandt.effectiveit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import ch.andrewrembrandt.effectiveit.dto.ProductDataDTO;
import ch.andrewrembrandt.effectiveit.service.ProductService;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
public class TenantAwareRepoTest extends R2dbcIntegrationTestInitialiser
    implements BaseTenantAwareTest {
  @Autowired ProductService productService;

  @Test
  void queryResultsFilteredToTenantAreReturned() {
    StepVerifier.create(setDefaultTenant(productService.getAllActiveProducts()))
        .assertNext(p -> assertThat(p.getSku()).isEqualTo("A1213"))
        .verifyComplete();
    StepVerifier.create(addTenant(productService.getAllActiveProducts(), 2l))
        .assertNext(p -> assertThat(p.getSku()).isEqualTo("A1214"))
        .verifyComplete();
  }

  @Test
  void newRowsUseTenantContext() {
    StepVerifier.create(setEmptyTenant(productService.getAllActiveProducts()))
        .verifyComplete();

    val date = LocalDate.now();
    StepVerifier.create(
            setEmptyTenant(
                productService.createProduct(
                    "NEW-SKU-111",
                    ProductDataDTO.builder()
                        .creationDate(date)
                        .name("New Product")
                        .price(new BigDecimal("34.65"))
                        .build())))
        .verifyComplete();

    StepVerifier.create(setEmptyTenant(productService.getAllActiveProducts()))
        .assertNext(p -> assertThat(p.getSku()).isEqualTo("NEW-SKU-111"))
        .verifyComplete();
  }
}
