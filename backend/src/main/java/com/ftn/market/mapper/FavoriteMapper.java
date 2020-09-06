package com.ftn.market.mapper;

import java.util.List;

import com.ftn.market.dto.favorite.FavoriteDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ftn.market.dto.favorite.FavoriteResponse;
import com.ftn.market.entity.Favorite;

@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = "spring",
    uses = { ShopMapper.class })
public abstract class FavoriteMapper {

  @Named("mapToFullResponse")
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "shopId", source = "shop.id")
  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "updatedAt")
  @Mapping(target = "deleted")
  public abstract FavoriteDto mapToFullResponse(Favorite favorite);

  @BeanMapping(ignoreByDefault = true)
  @IterableMapping(qualifiedByName = "mapToFullResponse")
  public abstract List<FavoriteDto> mapToFullListResponse(List<Favorite> favorites);
}
