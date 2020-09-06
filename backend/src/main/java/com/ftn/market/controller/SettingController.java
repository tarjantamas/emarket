package com.ftn.market.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.market.constants.ErrorCodes;
import com.ftn.market.dto.setting.SettingResponse;
import com.ftn.market.dto.setting.UpdateSetting;
import com.ftn.market.entity.Setting;
import com.ftn.market.mapper.SettingMapper;
import com.ftn.market.service.SettingService;
import com.ftn.market.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/setting")
public class SettingController {

  private final SettingMapper settingMapper;
  private final UserService userService;
  private final SettingService settingService;

  @GetMapping(value = "/my", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<SettingResponse> findMySettings() {
    return ResponseEntity.ok(settingMapper.mapToFullResponse(
        settingService.findForUser(userService.getActiveUserOrFail(), ErrorCodes.SETTING_NOT_FOUND_1)));
  }

  @PutMapping(path = "/my", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<SettingResponse> updateMySettings(@RequestBody final UpdateSetting updateSetting) {
    final Setting setting = settingService.updateForUser(userService.getActiveUserOrFail(), updateSetting);

    return ResponseEntity.ok(settingMapper.mapToFullResponse(setting));
  }
}
