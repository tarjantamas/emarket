package com.ftn.market.dto.shop;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShop {

  private String name;

  private String description;

  private Double latitude;

  private Double longitude;

  private List<@NotNull Long> productIds;
}
