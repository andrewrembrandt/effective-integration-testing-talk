package ch.andrewrembrandt.effectiveit.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDTO {
  private Long orderId;
  private List<ProductDTO> products;
  private String buyerEmail;
  private ZonedDateTime placedTime;
  private BigDecimal total;
}
