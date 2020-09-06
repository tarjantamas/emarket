package com.ftn.market.integration.setting;

import static com.ftn.market.util.SettingTestUtils.buildValidForUpdate;
import static com.ftn.market.util.SettingTestUtils.verifyReturnedSettingResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ftn.market.BaseIntegrationTestSetup;
import com.ftn.market.TestHelper;
import com.ftn.market.dto.setting.SettingResponse;
import com.ftn.market.dto.setting.UpdateSetting;
import com.ftn.market.entity.Setting;

public class UpdateMySettingsTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/setting";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void shouldReturnOkAndUpdatedSetting() {
    final UpdateSetting updateSetting = buildValidForUpdate();

    final ResponseEntity<SettingResponse> settingResponse = testRestTemplate.exchange(URL + "/my", HttpMethod.PUT,
        testHelper.createRequestEntityRegisteredUser(updateSetting), SettingResponse.class);

    assertThat(settingResponse.getStatusCode(), is(HttpStatus.OK));
    final SettingResponse actual = settingResponse.getBody();
    assertNotNull(actual);
    final Setting expected = entityManager.find(Setting.class, 2L);
    assertNotNull(actual);
    assertNotNull(expected);
    verifyReturnedSettingResponse(actual, expected);
  }
}
