package com.ftn.market;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ftn.market.config.security.JwtRequest;
import com.ftn.market.config.security.JwtResponse;

@Service
public class TestHelper {

  private HttpHeaders adminHeaders;

  private HttpHeaders registeredUserHeaders;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @PostConstruct
  public void initialize() {
    final HttpEntity adminLoginEntity = new HttpEntity<>(new JwtRequest("admin1@market.com", "password"));
    final ResponseEntity<JwtResponse> adminLoginResponse = testRestTemplate.exchange("http://34.89.182.11:6996/login",
        HttpMethod.POST, adminLoginEntity, JwtResponse.class, Collections.emptyMap());

    final HttpEntity registeredUserLoginEntity = new HttpEntity<>(new JwtRequest("user1@market.com", "password"));
    final ResponseEntity<JwtResponse> registeredUserResponse =
        testRestTemplate.exchange("http://34.89.182.11:6996/login", HttpMethod.POST, registeredUserLoginEntity,
            JwtResponse.class, Collections.emptyMap());

    assertEquals(HttpStatus.OK, adminLoginResponse.getStatusCode());
    assertNotNull(adminLoginResponse.getBody());

    assertEquals(HttpStatus.OK, registeredUserResponse.getStatusCode());
    assertNotNull(registeredUserResponse.getBody());

    adminHeaders = new HttpHeaders();
    adminHeaders.add("Authorization", "Bearer " + adminLoginResponse.getBody().getToken());

    registeredUserHeaders = new HttpHeaders();
    registeredUserHeaders.add("Authorization", "Bearer " + registeredUserResponse.getBody().getToken());
  }

  public HttpEntity createRequestEntityWithoutAuthorization(final Object body) {
    return new HttpEntity<>(body);
  }

  public HttpEntity createRequestEntityRegisteredUser(final Object body) {
    return new HttpEntity<>(body, registeredUserHeaders);
  }

  public HttpEntity createRequestEntityRegisteredUser() {
    return new HttpEntity<>(registeredUserHeaders);
  }

  public HttpEntity createRequestEntityAdminUser(final Object body) {
    return new HttpEntity<>(body, adminHeaders);
  }

  public HttpEntity createRequestEntityAdminUser() {
    return new HttpEntity<>(adminHeaders);
  }
}
