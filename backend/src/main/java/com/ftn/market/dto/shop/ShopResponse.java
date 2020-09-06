package com.ftn.market.dto.shop;

import lombok.Data;

@Data
public class ShopResponse {

  private Long id;

  private String name;

  private String description;

  private Double latitude;

  private Double longitude;

  private Long companyId;
}
