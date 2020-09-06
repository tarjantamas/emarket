package com.ftn.market.dto.setting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSetting {

  private Boolean syncEnabled;

  private Boolean locationTrackingAllowed;

  private Double searchRadius;

  private Boolean emailsEnabled;
}
