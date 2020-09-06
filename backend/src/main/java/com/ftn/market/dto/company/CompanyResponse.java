package com.ftn.market.dto.company;

import java.util.List;

import lombok.Data;

@Data
public class CompanyResponse {

  private Long id;

  private String name;

  private String description;

  private Integer vat;

  private Integer rid;

  private String country;

  private String city;

  private String address;

  private Long userId;

  private List<Long> shopIds;
}
