package com.ftn.market.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ftn.market.dto.image.ImageResponse;
import com.ftn.market.entity.Image;

@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = "spring")
public abstract class ImageMapper {

  @Named("mapToFullResponse")
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id")
  public abstract ImageResponse mapToFullResponse(Image image);

  @BeanMapping(ignoreByDefault = true)
  @IterableMapping(qualifiedByName = "mapToFullResponse")
  public abstract List<ImageResponse> mapToFullListResponse(List<Image> images);
}
