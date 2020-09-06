package com.tim32.emarket.apiclients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopUpdate {

    private String name;

    private String description;

    private Double latitude;

    private Double longitude;

    private List<Long> productIds;
}
