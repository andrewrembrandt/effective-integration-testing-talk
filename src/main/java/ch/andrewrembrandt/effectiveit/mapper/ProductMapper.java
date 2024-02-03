package ch.andrewrembrandt.effectiveit.mapper;

import ch.andrewrembrandt.effectiveit.dto.ProductDTO;
import ch.andrewrembrandt.effectiveit.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
  @Mapping(source = "name", target = "name")
  ProductDTO toProductDto(Product product);

  @Mapping(source = "name", target = "name")
  Product toProduct(ProductDTO dto);
}
