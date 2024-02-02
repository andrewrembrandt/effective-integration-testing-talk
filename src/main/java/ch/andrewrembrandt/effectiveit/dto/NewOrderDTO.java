package ch.andrewrembrandt.effectiveit.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewOrderDTO {
  private List<String> productSkus;
  private String buyerEmail;
}
