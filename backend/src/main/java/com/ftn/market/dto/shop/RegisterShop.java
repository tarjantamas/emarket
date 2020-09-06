package com.ftn.market.dto.shop;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterShop {

  @NotNull
  private String name;

  @NotNull
  private String description;

  @NotNull
  private Double latitude;

  @NotNull
  private Double longitude;

  @NotNull
  private Long companyId;
}
