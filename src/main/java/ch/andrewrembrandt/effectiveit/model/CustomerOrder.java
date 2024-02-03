package ch.andrewrembrandt.effectiveit.model;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class CustomerOrder {
  @Id private Long id;
  private String buyerEmail;
  private ZonedDateTime placedTime;
}
