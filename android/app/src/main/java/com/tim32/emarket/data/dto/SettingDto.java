package com.tim32.emarket.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingDto {

    private Long id;

    private Boolean syncEnabled;

    private Boolean locationTrackingAllowed;

    private Double searchRadius;

    private Boolean emailsEnabled;

    private Date updatedAt;
}
