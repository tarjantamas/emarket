package com.ftn.market.dto.company;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCompany {

  @NotNull
  private String name;

  @NotNull
  private String description;

  @NotNull
  private Integer vat;

  @NotNull
  private Integer rid;

  @NotNull
  private String country;

  @NotNull
  private String city;

  @NotNull
  private String address;
}
