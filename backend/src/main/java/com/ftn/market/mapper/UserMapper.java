package com.ftn.market.mapper;

import org.mapstruct.*;

import com.ftn.market.dto.user.RegisterUser;
import com.ftn.market.dto.user.UpdateUser;
import com.ftn.market.dto.user.UserResponse;
import com.ftn.market.entity.User;

@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = "spring")
public abstract class UserMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "email")
  @Mapping(target = "firstName")
  @Mapping(target = "lastName")
  public abstract User mapToDBO(RegisterUser registerUser);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id")
  @Mapping(target = "email")
  @Mapping(target = "firstName")
  @Mapping(target = "lastName")
  @Mapping(target = "companyIds")
  public abstract UserResponse mapToFullResponse(User user);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "firstName")
  @Mapping(target = "lastName")
  public abstract void mapToDBO(UpdateUser updateUser, @MappingTarget User user);
}
