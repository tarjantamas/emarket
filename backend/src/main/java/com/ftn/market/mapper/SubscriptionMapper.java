package com.ftn.market.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ftn.market.dto.subscription.SubscriptionResponse;
import com.ftn.market.entity.Subscription;

@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = "spring",
    uses = { CompanyMapper.class })
public abstract class SubscriptionMapper {

  @Named("mapToFullResponse")
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "company", qualifiedByName = "mapToFullResponseCompany")
  public abstract SubscriptionResponse mapToFullResponse(Subscription subscription);

  @BeanMapping(ignoreByDefault = true)
  @IterableMapping(qualifiedByName = "mapToFullResponse")
  public abstract List<SubscriptionResponse> mapToFullListResponse(List<Subscription> subscriptions);
}
