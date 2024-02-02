package ch.andrewrembrandt.effectiveit;

import static org.assertj.core.api.Assertions.assertThat;

import ch.andrewrembrandt.effectiveit.dto.ProductDTO;
import ch.andrewrembrandt.effectiveit.dto.ProductDataDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.shaded.com.google.common.collect.Iterables;

@SpringBootTest
@AutoConfigureWebTestClient
public class ProductControllerIntegrationTest {

  private final ProductDataDTO testPdDto = new ProductDataDTO("", BigDecimal.ZERO, LocalDate.now());
  @Autowired private WebTestClient client;

  @Test
  void productsReturned() {
    ensureNProducts(2);
  }

  @Test
  void addProduct() {
    val products = getProducts();

    val newProduct = new ProductDataDTO("Awesome", new BigDecimal("3000.1"), LocalDate.now());
    client
        .post()
        .uri("/api/products/B2000")
        .body(BodyInserters.fromValue(newProduct))
        .exchange()
        .expectStatus()
        .isOk();

    products.add(
        new ProductDTO(
            "B2000", newProduct.getName(), newProduct.getPrice(), newProduct.getCreationDate()));
    ensureResultsContain(products);
  }

  @Test
  void updateProduct() {
    val existing = getProducts().stream().filter(p -> !p.getSku().equals("A1213"));

    val updatedProduct =
        new ProductDataDTO("Neues Produkt", new BigDecimal("200.0"), LocalDate.now());
    client
        .put()
        .uri("/api/products/A1213")
        .body(BodyInserters.fromValue(updatedProduct))
        .exchange()
        .expectStatus()
        .isOk();

    val expected = existing.collect(Collectors.toList());
    expected.add(
        new ProductDTO(
            "A1213",
            updatedProduct.getName(),
            updatedProduct.getPrice(),
            updatedProduct.getCreationDate()));
    ensureResultsContain(expected);
  }

  @Test
  void updateNonExistentSku404() {
    client
        .put()
        .uri("/api/products/A1215")
        .body(BodyInserters.fromValue(testPdDto))
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  void createExistingSku409() {
    client
        .post()
        .uri("/api/products/A1214")
        .body(BodyInserters.fromValue(testPdDto))
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.CONFLICT);
  }

  private void ensureNProducts(int numProducts) {
    client
        .get()
        .uri("/api/products")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(ProductDTO.class)
        .hasSize(numProducts);
  }

  private void ensureResultsContain(Iterable<ProductDTO> expected) {
    val results = getProducts();
    assertThat(results).containsOnly(Iterables.toArray(expected, ProductDTO.class));
  }

  private List<ProductDTO> getProducts() {
    return client
        .get()
        .uri("/api/products")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(ProductDTO.class)
        .returnResult()
        .getResponseBody();
  }
}
