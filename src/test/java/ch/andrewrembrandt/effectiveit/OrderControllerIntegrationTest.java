package ch.andrewrembrandt.effectiveit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;

import ch.andrewrembrandt.effectiveit.dto.NewOrderDTO;
import ch.andrewrembrandt.effectiveit.dto.OrderDTO;
import ch.andrewrembrandt.effectiveit.dto.ProductDTO;
import ch.andrewrembrandt.effectiveit.util.R2dbcIntegrationTestInitialiser;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest
@AutoConfigureWebTestClient
@Import(R2dbcIntegrationTestInitialiser.class)
public class OrderControllerIntegrationTest {

  LocalDate watchDate = LocalDate.parse("2001-01-01");
  LocalDate tabletDate = LocalDate.parse("2001-01-02");
  ProductDTO watch = new ProductDTO("A1213", "Watch", new BigDecimal("30.5"), watchDate);
  OrderDTO secondOrder =
      new OrderDTO(
          2001L,
          list(watch),
          "myself@me.com",
          ZonedDateTime.parse("2015-01-01T09:00Z[UTC]"),
          new BigDecimal("30.5"));
  ProductDTO tablet = new ProductDTO("A1214", "Tablet", new BigDecimal("700.9"), tabletDate);
  OrderDTO firstOrder =
      new OrderDTO(
          2000L,
          list(watch, watch, tablet),
          "me@me.com",
          ZonedDateTime.parse("2011-01-01T19:00Z[UTC]"),
          new BigDecimal("761.9"));
  @Autowired private WebTestClient client;

  @Test
  void allOrdersReturned() {
    val from = ZonedDateTime.parse("2001-04-23T04:30:45+01:00");
    val to = from.plusYears(20);
    val results = getOrderResults(from, to);

    assertThat(results).containsOnly(array(firstOrder, secondOrder));
  }

  @Test
  void ordersByDateAreFiltered() {
    val fromFirst = ZonedDateTime.parse("2001-04-23T04:30:45+01:00");
    val toFirst = fromFirst.plusYears(11);

    val firstResults = getOrderResults(fromFirst, toFirst);
    assertThat(firstResults).containsOnly(array(firstOrder));

    val toSecond = toFirst.plusYears(4);
    val secondResults = getOrderResults(toFirst, toSecond);
    assertThat(secondResults).containsOnly(array(secondOrder));
  }

  @Test
  void addOrder() {
    val curTime = ZonedDateTime.now();

    val newOrder = new NewOrderDTO(list("A1214", "A1214"), "i@me.com");
    createOrder(newOrder).expectStatus().isOk();

    var results = getOrderResults(curTime, curTime.plusMinutes(1));
    assertThat(results)
        .extracting("products", "buyerEmail", "total")
        .contains(
            tuple(
                list(tablet, tablet),
                newOrder.getBuyerEmail(),
                tablet.getPrice().multiply(BigDecimal.valueOf(2))));
  }

  @Test
  void addNonExistentSku400() {
    val newOrder = new NewOrderDTO(list("B1215"), "and@me.com");

    createOrder(newOrder).expectStatus().isBadRequest();
  }

  private String datetimeToUriParam(ZonedDateTime dt) {
    return dt.withZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME);
  }

  private List<OrderDTO> getOrderResults(ZonedDateTime from, ZonedDateTime to) {
    return client
        .get()
        .uri("/api/orders?from=" + datetimeToUriParam(from) + "&to=" + datetimeToUriParam(to))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(OrderDTO.class)
        .returnResult()
        .getResponseBody();
  }

  private WebTestClient.ResponseSpec createOrder(NewOrderDTO newOrder) {
    return client.post().uri("/api/orders").body(BodyInserters.fromValue(newOrder)).exchange();
  }
}
