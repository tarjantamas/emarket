package com.ftn.market.dto.setting;

import lombok.Data;

import java.util.Date;

@Data
public class SettingResponse {

  private Long id;

  private Boolean syncEnabled;

  private Boolean locationTrackingAllowed;

  private Double searchRadius;

  private Boolean emailsEnabled;

  private Date updatedAt;
}
