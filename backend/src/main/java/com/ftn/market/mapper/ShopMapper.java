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

import com.ftn.market.dto.shop.RegisterShop;
import com.ftn.market.dto.shop.ShopResponse;
import com.ftn.market.dto.shop.UpdateShop;
import com.ftn.market.entity.Shop;

@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = "spring")
public abstract class ShopMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "name")
  @Mapping(target = "description")
  @Mapping(target = "latitude")
  @Mapping(target = "longitude")
  public abstract Shop mapToDBO(RegisterShop registerShop);

  @BeanMapping(ignoreByDefault = true)
  @Named("mapToFullResponseShop")
  @Mapping(target = "id")
  @Mapping(target = "name")
  @Mapping(target = "description")
  @Mapping(target = "latitude")
  @Mapping(target = "longitude")
  @Mapping(target = "companyId", source = "company.id")
  public abstract ShopResponse mapToFullResponse(Shop shop);

  @BeanMapping(ignoreByDefault = true)
  @IterableMapping(qualifiedByName = "mapToFullResponseShop")
  public abstract List<ShopResponse> mapToFullListResponse(List<Shop> shops);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "name")
  @Mapping(target = "description")
  @Mapping(target = "latitude")
  @Mapping(target = "longitude")
  public abstract void mapToDBO(UpdateShop updateShop, @MappingTarget Shop shop);
}
