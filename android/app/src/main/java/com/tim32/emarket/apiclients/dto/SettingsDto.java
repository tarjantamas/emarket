package com.tim32.emarket.apiclients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDto {

    private Long id;

    private boolean syncEnabled;

    private boolean locationTrackingAllowed;

    private double searchRadius;

    private boolean emailsEnabled;

}
