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

import com.ftn.market.dto.company.CompanyResponse;
import com.ftn.market.dto.company.RegisterCompany;
import com.ftn.market.dto.company.UpdateCompany;
import com.ftn.market.entity.Company;

@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = "spring")
public abstract class CompanyMapper {

  @Named("mapToFullResponseCompany")
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id")
  @Mapping(target = "name")
  @Mapping(target = "description")
  @Mapping(target = "vat")
  @Mapping(target = "rid")
  @Mapping(target = "country")
  @Mapping(target = "city")
  @Mapping(target = "address")
  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "shopIds")
  public abstract CompanyResponse mapToFullResponse(Company company);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "name")
  @Mapping(target = "description")
  @Mapping(target = "vat")
  @Mapping(target = "rid")
  @Mapping(target = "country")
  @Mapping(target = "city")
  @Mapping(target = "address")
  public abstract Company mapToDBO(RegisterCompany registerCompany);

  @BeanMapping(ignoreByDefault = true)
  @IterableMapping(qualifiedByName = "mapToFullResponseCompany")
  public abstract List<CompanyResponse> mapToFullListResponse(List<Company> companies);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "name")
  @Mapping(target = "description")
  @Mapping(target = "vat")
  @Mapping(target = "rid")
  @Mapping(target = "country")
  @Mapping(target = "city")
  @Mapping(target = "address")
  public abstract void mapToDBO(UpdateCompany updateCompany, @MappingTarget Company company);
}
