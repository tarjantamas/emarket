package com.ftn.market.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.market.service.FavoriteService;
import com.ftn.market.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite")
public class FavoriteController {

  private final FavoriteService favoriteService;
  private final UserService userService;

  @DeleteMapping(path = "/shop/{shopId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> removeShopFromFavorites(@PathVariable final long shopId) {
    favoriteService.removeShopFromFavorites(userService.getActiveUserOrFail(), shopId);

    return ResponseEntity.noContent().build();
  }
}
