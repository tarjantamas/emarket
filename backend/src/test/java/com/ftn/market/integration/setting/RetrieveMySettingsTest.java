package com.ftn.market.integration.setting;

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
import com.ftn.market.entity.Setting;

public class RetrieveMySettingsTest extends BaseIntegrationTestSetup {

  private static final String URL = "/api/v1/setting";

  @Autowired
  private TestHelper testHelper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private EntityManager entityManager;

  @Test
  public void shouldReturnOkAndMySetting() {
    final ResponseEntity<SettingResponse> settingResponse = testRestTemplate.exchange(URL + "/my", HttpMethod.GET,
        testHelper.createRequestEntityRegisteredUser(), SettingResponse.class);

    assertThat(settingResponse.getStatusCode(), is(HttpStatus.OK));
    final SettingResponse actual = settingResponse.getBody();
    final Setting expected = entityManager.find(Setting.class, 2L);
    assertNotNull(actual);
    assertNotNull(expected);
    verifyReturnedSettingResponse(actual, expected);
  }
}
