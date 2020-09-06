package com.ftn.market.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.market.dto.subscription.SubscriptionResponse;
import com.ftn.market.mapper.SubscriptionMapper;
import com.ftn.market.service.SubscriptionService;
import com.ftn.market.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionsController {

  private final SubscriptionMapper subscriptionMapper;
  private final SubscriptionService subscriptionService;
  private final UserService userService;

  @PutMapping(path = "/company/{companyId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> subscribeToCompany(@PathVariable final long companyId) {
    subscriptionService.subscribeToCompany(userService.getActiveUserOrFail(), companyId);

    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/companies", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<SubscriptionResponse>> findCompanySubscriptions() {
    return ResponseEntity.ok(subscriptionMapper
      .mapToFullListResponse(subscriptionService.findCompanySubscriptions(userService.getActiveUserOrFail())));
  }
}
