package ch.andrewrembrandt.effectiveit;

import static org.assertj.core.util.Lists.list;
import static org.mockito.ArgumentMatchers.any;

import ch.andrewrembrandt.effectiveit.dto.NewOrderDTO;
import ch.andrewrembrandt.effectiveit.repository.OrderProductRepository;
import ch.andrewrembrandt.effectiveit.repository.OrderRepository;
import ch.andrewrembrandt.effectiveit.service.OrderService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class OrderServiceTest implements BaseTenantAwareTest {
  @MockBean
  OrderRepository orderRepo;

  @MockBean OrderProductRepository orderProductRepo;

  @Test
  void addOrder() {
    val newOrderDto = new NewOrderDTO(list("A1213", "A1214"), "who@me.com");
    Mockito.when(orderRepo.save(any())).thenReturn(Mono.empty());
    Mockito.when(orderProductRepo.addProductForOrder(any(), any(), any())).thenReturn(Mono.just(1));

    val orderService = new OrderService(orderRepo, orderProductRepo);
    val createMono = orderService.createOrder(newOrderDto);
    StepVerifier.create(addTenant(createMono, 1l)).verifyComplete();

    //    verify(orderRepo).save(eq(newOrderDto), any(ZonedDateTime.class));
    //    verify(orderProductRepo).addProductForOrder(any(), eq(newOrderDto.getProductSkus()));
  }
}
