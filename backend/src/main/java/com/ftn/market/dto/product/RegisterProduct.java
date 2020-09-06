package com.ftn.market.dto.product;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterProduct {

  @NotNull
  private String name;

  @NotNull
  private String description;

  @NotNull
  private Double price;

  @NotNull
  private Long companyId;

  @NotNull
  private String unit;
}
