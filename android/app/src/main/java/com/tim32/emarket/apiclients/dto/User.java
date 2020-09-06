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
public class User {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private List<Long> companyIds;
}
