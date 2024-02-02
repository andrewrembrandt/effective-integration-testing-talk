package ch.andrewrembrandt.effectiveit.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class NoProductsException extends RuntimeException {
  private String order;
}
