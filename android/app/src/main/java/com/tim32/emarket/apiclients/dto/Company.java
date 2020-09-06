package com.tim32.emarket.apiclients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    private long id;

    private String name;

    private String description;

    private int vat;

    private int rid;

    private String country;

    private String city;

    private String address;

    private Long userId;

    private List<Long> shopIds;

}
