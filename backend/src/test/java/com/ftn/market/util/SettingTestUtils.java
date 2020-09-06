package com.ftn.market.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.ftn.market.dto.setting.SettingResponse;
import com.ftn.market.dto.setting.UpdateSetting;
import com.ftn.market.entity.Setting;

public class SettingTestUtils extends TestUtils {

  public static void verifyReturnedSettingResponse(final SettingResponse actual, final Setting expected) {
    assertThat(actual.getId(), is(expected.getId()));
    assertThat(actual.getEmailsEnabled(), is(expected.getEmailsEnabled()));
    assertThat(actual.getLocationTrackingAllowed(), is(expected.getLocationTrackingAllowed()));
    assertThat(actual.getSearchRadius(), is(expected.getSearchRadius()));
    assertThat(actual.getSyncEnabled(), is(expected.getSyncEnabled()));
  }

  public static UpdateSetting buildValidForUpdate() {
    return UpdateSetting.builder()
      .emailsEnabled(true)
      .locationTrackingAllowed(true)
      .searchRadius(15.5)
      .syncEnabled(true)
      .build();
  }
}
