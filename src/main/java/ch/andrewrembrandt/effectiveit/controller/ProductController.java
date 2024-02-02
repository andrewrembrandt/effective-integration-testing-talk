package ch.andrewrembrandt.effectiveit.controller;

import ch.andrewrembrandt.effectiveit.dto.ProductDTO;
import ch.andrewrembrandt.effectiveit.dto.ProductDataDTO;
import ch.andrewrembrandt.effectiveit.service.ProductService;
import ch.andrewrembrandt.effectiveit.util.SkuAlreadyExistsException;
import ch.andrewrembrandt.effectiveit.util.SkuNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Products CRUD API")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation")})
public class ProductController {

  private final ProductService productService;

  @GetMapping
  @Operation(
      summary = "All current Products",
      description = "Returns all of the active (non deleted) products")
  Flux<ProductDTO> getAll() {
    return productService.getAllActiveProducts();
  }

  @PostMapping(path = "/{sku}")
  @Operation(
      summary = "Create/add a new product",
      description = "Creates a new product - requires an SKU that has not been previously used")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "409",
            description = "Conflict - the sku is already in use",
            content = @Content())
      })
  Mono<Void> create(@PathVariable String sku, @RequestBody ProductDataDTO dto) {
    return productService.createProduct(sku, dto);
  }

  @PutMapping(path = "/{sku}")
  @Operation(
      summary = "Update an existing product",
      description = "Allows changing all parameters except the sku")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "404",
            description = "Not found - the active sku was not found",
            content = @Content())
      })
  Mono<Void> update(@PathVariable String sku, @RequestBody ProductDataDTO dto) {
    return productService.updateProduct(sku, dto);
  }

  @DeleteMapping(path = "/{sku}")
  @Operation(summary = "Delete a product", description = "'Soft' deletes an existing product")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "404",
            description = "Not found - the active sku was not found",
            content = @Content())
      })
  Mono<Void> delete(@PathVariable String sku) {
    return productService.deleteProduct(sku);
  }

  @ExceptionHandler
  ResponseEntity<String> handle(SkuNotFoundException skuException) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("SKU: " + skuException.getSku() + " not found.");
  }

  @ExceptionHandler
  ResponseEntity<String> handle(SkuAlreadyExistsException skuException) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body("SKU: " + skuException.getSku() + " already exists.");
  }
}
