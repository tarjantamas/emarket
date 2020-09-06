package com.tim32.emarket.apiclients.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterCompany {

    public String name;

    public String description;

    public Integer vat;

    public Integer rid;

    public String country;

    public String city;

    public String address;
}
