package com.ftn.market.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ftn.market.dto.product.ProductResponse;
import com.ftn.market.dto.product.RegisterProduct;
import com.ftn.market.dto.product.UpdateProduct;
import com.ftn.market.entity.Product;

@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = "spring")
public abstract class ProductMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "name")
  @Mapping(target = "description")
  @Mapping(target = "price")
  @Mapping(target = "unit")
  public abstract Product mapToDBO(RegisterProduct registerProduct);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "name")
  @Mapping(target = "description")
  @Mapping(target = "price")
  @Mapping(target = "unit")
  public abstract void mapToDBO(UpdateProduct updateProduct, @MappingTarget Product product);

  @Named("mapToFullResponse")
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id")
  @Mapping(target = "name")
  @Mapping(target = "description")
  @Mapping(target = "price")
  @Mapping(target = "unit")
  @Mapping(target = "companyId", source = "company.id")
  @Mapping(target = "shopIds")
  public abstract ProductResponse mapToFullResponse(Product product);

  @BeanMapping(ignoreByDefault = true)
  @IterableMapping(qualifiedByName = "mapToFullResponse")
  public abstract List<ProductResponse> mapToFullListResponse(List<Product> products);
}
