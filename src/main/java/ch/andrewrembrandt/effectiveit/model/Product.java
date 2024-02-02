package ch.andrewrembrandt.effectiveit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends TenantableEntity {
  @Id
  private Long id;
  private String sku;
  private String name;
  private BigDecimal price;
  private LocalDate creationDate;
  private Boolean deleted;
}
