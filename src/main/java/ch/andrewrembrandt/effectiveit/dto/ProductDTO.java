package ch.andrewrembrandt.effectiveit.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
  private String sku;
  private String name;
  private BigDecimal price;
  private LocalDate creationDate;
}
