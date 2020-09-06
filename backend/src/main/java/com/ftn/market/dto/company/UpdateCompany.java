package com.ftn.market.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompany {

  private String name;

  private String description;

  private Integer vat;

  private Integer rid;

  private String country;

  private String city;

  private String address;
}
