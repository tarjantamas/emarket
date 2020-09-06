package com.ftn.market.dto.product;

import java.util.List;

import lombok.Data;

@Data
public class ProductResponse {

  private Long id;

  private String name;

  private String description;

  private String unit;

  private Double price;

  private Long companyId;

  private List<Long> shopIds;
}
