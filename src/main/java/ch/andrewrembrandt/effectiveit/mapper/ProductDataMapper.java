package ch.andrewrembrandt.effectiveit.mapper;

import ch.andrewrembrandt.effectiveit.dto.ProductDataDTO;
import ch.andrewrembrandt.effectiveit.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductDataMapper {
  @Mapping(source="name", target = "name")
  ProductDataDTO toProductDataDto(Product product);
  @Mapping(source="name", target = "name")
  Product toProduct(ProductDataDTO dto);
}
