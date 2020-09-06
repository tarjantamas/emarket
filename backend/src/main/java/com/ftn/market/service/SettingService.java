package com.ftn.market.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.market.constants.ErrorCodes;
import com.ftn.market.dto.setting.UpdateSetting;
import com.ftn.market.entity.Setting;
import com.ftn.market.entity.User;
import com.ftn.market.exception.NotFoundException;
import com.ftn.market.mapper.SettingMapper;
import com.ftn.market.repository.SettingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingService {

  private final SettingMapper settingMapper;
  private final SettingRepository settingRepository;

  @Transactional
  public void create(final User user) {
    final Setting setting = new Setting();
    setting.setUser(user);
    setting.setUpdatedAt(new Date());

    settingRepository.save(setting);
    log.info("Successfully created {} for {}", setting, user);
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public Setting findForUser(final User user, final String errorCode) {
    final Optional<Setting> oSetting = settingRepository.findByUser(user);

    if (!oSetting.isPresent()) {
      log.info("Product for '{}' doesn't exist.", user);
      throw new NotFoundException(errorCode);
    }
    return oSetting.get();
  }

  @Transactional
  public Setting updateForUser(final User user, final UpdateSetting updateSetting) {
    Setting existingSetting = findForUser(user, ErrorCodes.SETTING_NOT_FOUND_2);

    settingMapper.mapToDBO(updateSetting, existingSetting);
    existingSetting.setUpdatedAt(new Date());

    existingSetting = settingRepository.save(existingSetting);
    log.info("{} successfully updated {}", user, existingSetting);

    return existingSetting;
  }
}
