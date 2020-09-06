package com.tim32.emarket.apiclients.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUser {

    private String email;

    private String password;

    private String firstName;

    private String lastName;
}
