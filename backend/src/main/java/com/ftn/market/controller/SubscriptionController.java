package com.ftn.market.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.market.service.SubscriptionService;
import com.ftn.market.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscription")
public class SubscriptionController {

  private final SubscriptionService subscriptionService;
  private final UserService userService;

  @DeleteMapping(path = "/company/{companyId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> removeShopFromFavorites(@PathVariable final long companyId) {
    subscriptionService.unsubscribeFromCompany(userService.getActiveUserOrFail(), companyId);

    return ResponseEntity.noContent().build();
  }
}
