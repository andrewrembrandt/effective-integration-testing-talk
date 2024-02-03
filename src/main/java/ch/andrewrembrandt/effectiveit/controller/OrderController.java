package ch.andrewrembrandt.effectiveit.controller;

import ch.andrewrembrandt.effectiveit.dto.NewOrderDTO;
import ch.andrewrembrandt.effectiveit.dto.OrderDTO;
import ch.andrewrembrandt.effectiveit.service.OrderService;
import ch.andrewrembrandt.effectiveit.util.NoProductsException;
import ch.andrewrembrandt.effectiveit.util.SkuNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Orders Basic API")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation")})
public class OrderController {

  private final OrderService orderService;

  @GetMapping
  @Operation(
      summary = "Orders within a date/time range",
      description = "Returns all between the from and to date/times (inclusive)")
  Flux<OrderDTO> getOrdersBetween(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
    return orderService.getOrdersBetween(
        from.atStartOfDay(ZoneId.systemDefault()),
        to.plusDays(1).atStartOfDay(ZoneId.systemDefault()));
  }

  @PostMapping
  @Operation(
      summary = "Create/add a new order",
      description =
          "Creates a new order with the referenced product SKUs - SKUs can be repeated for quantities > 1")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "400",
            description = "Bad request - supplied data is invalid",
            content = @Content())
      })
  Mono<Void> create(@RequestBody NewOrderDTO dto) {
    return orderService.createOrder(dto);
  }

  @ExceptionHandler
  ResponseEntity<String> handle(SkuNotFoundException skuException) {
    return ResponseEntity.badRequest()
        .body("Order for creation contains invalid SKUs: " + skuException.getSku());
  }

  @ExceptionHandler
  ResponseEntity<String> handle(NoProductsException noProductsException) {
    return ResponseEntity.badRequest()
        .body(
            "Order for creation must contain at least one SKU: " + noProductsException.getOrder());
  }
}
