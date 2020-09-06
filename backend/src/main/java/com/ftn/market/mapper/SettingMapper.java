package com.ftn.market.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ftn.market.dto.setting.SettingResponse;
import com.ftn.market.dto.setting.UpdateSetting;
import com.ftn.market.entity.Setting;

@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = "spring")
public abstract class SettingMapper {

  @Named("mapToFullResponse")
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id")
  @Mapping(target = "syncEnabled")
  @Mapping(target = "locationTrackingAllowed")
  @Mapping(target = "searchRadius")
  @Mapping(target = "emailsEnabled")
  @Mapping(target = "updatedAt")
  public abstract SettingResponse mapToFullResponse(Setting setting);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "syncEnabled")
  @Mapping(target = "locationTrackingAllowed")
  @Mapping(target = "searchRadius")
  @Mapping(target = "emailsEnabled")
  public abstract void mapToDBO(UpdateSetting updateSetting, @MappingTarget Setting setting);
}
